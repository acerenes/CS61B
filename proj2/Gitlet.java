import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Gitlet {

    /* Creating the WorldState object (WorldState.ser). */
    private static class WorldState implements Serializable {

        private int currCommit;
        private int numCommits;
        private String currBranch;
        private HashMap<String, Integer> branchHeads; 
        // --^ Branch name to head ID #. 
        private HashMap<String, ArrayList<Integer>> commitsByMessage; 
        // --^ Commit message to ID #. 

        private static final long serialVersionUID = 1L; 
        // Force the version number, so won't InvalidClassException when try to deseriazlie it.

        private WorldState() {
            // Prbly only create a new WorldState at very beginning.
            currCommit = 0;
            numCommits = 0;

            currBranch = "master";

            branchHeads = new HashMap<String, Integer>();
            branchHeads.put("master", 0);

            commitsByMessage = new HashMap<String, ArrayList<Integer>>();
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            arrayList.add(0);
            commitsByMessage.put("initial commit", arrayList);
        }

        public Integer getCurrCommit() {
            return this.currCommit;
        }

        public Integer getNumCommits() {
            return this.numCommits;
        }

        public Integer getHeadOfCurrBranch() {
            return this.branchHeads.get(currBranch);
        }

        private void updateCurrBranch(String newBranch) {
            this.currBranch = newBranch;
        }


        private void updateHeadPointer(int commitID) {
            this.currCommit = commitID;
        }

        private void updateNumCommits() {
            this.numCommits = this.numCommits + 1;
        }

        /* Used only for rebase, I would say. */
        private void newNumCommits(int newSum) {
            this.numCommits = newSum;
        }

        private void updateBranchHeads(int commitID) {
            branchHeads.put(currBranch, commitID);
        }

        private void updateCommitMessages(String commitMessage, int commitID) {
            ArrayList<Integer> arrayList;
            if (this.commitsByMessage.containsKey(commitMessage)) {
                // Has mapping already, so have to add to the array.
                arrayList = this.commitsByMessage.get(commitMessage);
            } else {
                // Create new mapping. 
                arrayList = new ArrayList<Integer>();
            }
            arrayList.add(commitID);
            commitsByMessage.put(commitMessage, arrayList); 
        }

        private void createNewBranch(String newBranchName) {
            // DOESN'T SWITCH TO NEW BRANCH.
            // The branch points to, like the same thing your current branch does.
            this.branchHeads.put(newBranchName, currCommit);
        }

        private HashMap<String, Integer> getBranchHeads() {
            return this.branchHeads;
        }

        private String getCurrBranch() {
            return this.currBranch;
        }

        private HashMap<String, ArrayList<Integer>> getCommitsByMessage() {
            return this.commitsByMessage;
        }

        
    }



    /* Creating the Staging info object (Staging.ser). */
    private static class Staging implements Serializable {

        private Set<String> filesToAdd;
        private Set<String> filesToRemove;

        private static final long serialVersionUID = 2L;

        private Staging() {
            // Only gonna create a new Staging at very beginning, I think. 
            filesToAdd = new HashSet<String>();
            filesToRemove = new HashSet<String>();
        }

        private boolean hasFilesToAdd() {
            return !filesToAdd.isEmpty();
        }

        private boolean hasFilesToRemove() {
            return !filesToRemove.isEmpty();
        }

        private void addFile(String fileName) {
            filesToAdd.add(fileName);
        }

        private void removeFile(String fileName) {
            filesToRemove.add(fileName);
        }

        private void emptyStagingInfo() {
            filesToAdd.clear();
            filesToRemove.clear();
        }

        private Set<String> getFilesToRemove() {
            return this.filesToRemove;
        }

        private Set<String> getFilesToAdd() {
            return this.filesToAdd;
        }


        private boolean addContains(String file) {
            return this.filesToAdd.contains(file);
        }

        private boolean removeContains(String file) {
            return this.filesToRemove.contains(file);
        }

        private void removeFromAdd(String file) {
            this.filesToAdd.remove(file);
        }

        private void removeFromRemove(String file) {
            this.filesToRemove.remove(file);
        }
    }


    /* Creating the Serializable class for each commit. */
    private static class CommitWrapper implements Serializable {

        private Integer commitID;
        private String commitTime;
        private String commitMessage;
        private boolean isRoot; 
        // --^ If true, no parent.
        private Integer parentCommit; 
        // --^ So I can use null for commit 0.
        private HashMap<String, Integer> storedFiles; 

        private static final long serialVersionUID = 3L;

        /* Special constructor for new message in interactive rebase. */
        private CommitWrapper(int newID, CommitWrapper toCopy, int parent, HashMap<String, Integer> neededChanges, String newMessage) {

            // Just like below's special constructor, but with different message. 

            this.commitMessage = newMessage;

            this.commitID = newID;

            this.isRoot = false; // Definitely - it has a parent - your initial commit won't be a rebase thing. 

            this.commitTime = toCopy.getCommitTime();

            this.parentCommit = parent;

            this.storedFiles = toCopy.getStoredFiles();
            // hERE. NEED TO CHANGE. NEED TO ADD IN THE CHANGES. 
            this.storedFiles.putAll(neededChanges);
        }


        /* Constructor to copy Commit Wrapper of another ID. */
        private CommitWrapper(int newID, CommitWrapper toCopy, int parent, HashMap<String, Integer> neededChanges) {

            // Copy everything, except commitID, maybe isRoot, and parentCommit. 
            this.commitMessage = toCopy.getCommitMessage();

            this.commitID = newID;

            this.isRoot = false; // Definitely - it has a parent - your initial commit won't be a rebase thing. 

            this.commitTime = toCopy.getCommitTime();

            this.parentCommit = parent;

            this.storedFiles = toCopy.getStoredFiles();
            // hERE. NEED TO CHANGE. NEED TO ADD IN THE CHANGES. 
            this.storedFiles.putAll(neededChanges);
        }


        /* Fixed constructor. The blood is the remnants of the mess. */
        private CommitWrapper(Integer commitNum, String message) {
            this.commitMessage = message;

            commitID = commitNum;
            if (commitNum == 0) {
                isRoot = true;
            } else {
                isRoot = false;
            }

            this.commitTime = calculateCommitTime();

            /* Grab parent commit ID from the file. */
            this.parentCommit = calculateParentCommit(this.isRoot);



            /* Create map of files in this commit folder. */
            // Take it from Staging.ser, yeah??
            // CONTENTS FROM TIME OF COMMIT (not add) ARE RECORDED



            // First, copy all parent's files. 
            this.storedFiles = parentsFiles(this.isRoot);

            // Then take out stuff in filesToRemove. 
            removeFiles(this.storedFiles, this.isRoot);

            // Take working directory versions of filesToAdd, stick in folder. 
            // Update the map while at it.
            addFilesToFolder(this.commitID, this.isRoot, this.storedFiles);



        }
        // FINISHED CONSTRUCTOR


        /* Add working directory files to folder, but also update map in CommitWrapper object. */
        private void addFilesToFolder(int commit, boolean root, HashMap<String, Integer> stored) {
            if (!root) {
                Staging stage = getStaging();

                if (stage.hasFilesToAdd()) {
                    for (String file : stage.getFilesToAdd()) {


                        // Create space in folder first.
                        String filePath = createFileExistence(commit, file);

                        // Copy it over - thanks to examples.javacodegeeks.com.
                        /*FileChannel inputChannel = null;
                        FileChannel outputChannel = null;*/
                        try {
                            FileChannel inputChannel = new FileInputStream(file).getChannel();
                            FileChannel outputChannel = new FileOutputStream(filePath).getChannel();
                            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                            inputChannel.close();
                            outputChannel.close();
                        } catch (IOException ex) {
                            System.out.println("Error - Could not copy files to commit folder.");
                            System.exit(1);
                        } 

                        // Update map. 
                        stored.put(file, commit);
                    }
                }
            }
        }

        private String createFileExistence(int id, String file) {
            // Okay, the thing is, the file might be in some crazy weird folder. 
            // Create weird directories to maintain this file name.
            // Stack overflow, HOPE THIS WORKS.
            String fileLocation = ".gitlet/snapshots/" + id + "/" + file;
            File newFile = new File(fileLocation);
            try {
                if (newFile.getParentFile() != null) {
                    newFile.getParentFile().mkdirs();
                } 
                FileWriter writer = new FileWriter(newFile);
            } catch (IOException ex) {
                System.out.println("Error - Could not create file space in commit folders.");
                ex.printStackTrace();
                System.exit(1);
            }
            return fileLocation;
        }

        private void removeFiles(HashMap<String, Integer> fileMap, boolean root) {
            // Commit 0 contains no files.
            if (!root) {
                Staging stage = getStaging();

                if (stage.hasFilesToRemove()) {
                    for (String file : stage.getFilesToRemove()) {
                        if (stage.addContains(file)) {
                            // Unstage it.
                            stage.removeFromAdd(file);
                        }
                        // Remember it no more. 
                        if (fileMap.containsKey(file)) {
                            fileMap.remove(file);
                        }
                    }
                }
            }
        }

        private HashMap<String, Integer> parentsFiles(boolean root) {
            if (!root) {
                // Takes lastCommitWrapper like, last commit in WorldState. 
                // Which is how we calculated parent commit, so I guess it's fine. 
                CommitWrapper parentCommitWrapper = lastCommitWrapper();
                return new HashMap<String, Integer>(parentCommitWrapper.getStoredFiles());
            }
            return new HashMap<String, Integer>();
        } 

        private String calculateCommitTime() {
            // Thanks to StackOverflow for showing me how to get the time.
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            commitTime = dateFormat.format(cal.getTime());
            return commitTime;
        }

        private Integer calculateParentCommit(boolean root) {
            if (!root) {
                // Calculate parent from WorldState.
                // DO NOT UPDATE WORLDSTATE TILL VERY END. 
                // So World State's current commit should be the parent. 

                // JK it should be the 
                return lastCommit();
            } 
            // If you're the root, you have no parent. I'M GOING TO SAY YOUR PARENT IS YOURSELF. 
            return 0;
        }
        

        public Integer getCommitID() {
            return this.commitID;
        }

        public String getCommitTime() {
            return this.commitTime;
        }

        public String getCommitMessage() {
            return this.commitMessage;
        }

        public Integer getParentCommit() {
            return this.parentCommit;
        }

        public HashMap<String, Integer> getStoredFiles() {
            return this.storedFiles;
        }

        public boolean isTracking(String file) {
            return this.storedFiles.containsKey(file);
        }

    }




    public static void main(String[] args) {
        
        String command = null; // --^ Initialize, else java mad.
        String input1 = null;
        String input2 = null; 
        if (args.length > 0) {
            command = args[0];
            if (args.length > 1) {
                input1 = args[1];
                if (args.length > 2) {
                    input2 = args[2];
                }
            }
        } else {
            System.out.println("Please enter a command.");
            return;
        }

        switch (command) {
            case "init":
                checkInitialize();
                break;

            case "add":
                // Can only add 1 file at a time.
                // Command line arguments split on space, I believe.
                checkAdd(input1);
                break;

            case "commit":
                 // Commit must have a non-blank message.
                checkCommit(input1);
                break;
            case "remove":
                checkRemove(input1);
                break;
            case "log":
                log();
                break;
            case "global-log":
                globalLog();
                break; 
            case "find":
                String findMessage = input1;
                if (findMessage != null) {
                    find(findMessage);
                } else {
                    System.out.println("Did not enter enough arguments.");
                }
                break;
            case "status":
                status();
                break;
            case "checkout":
                // Thanks to homeandlearn.co.uk for user input stuff. 
                Scanner userInput = new Scanner(System.in);
                System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)");
                String input = userInput.next();
                if (input.equals("yes")) {
                    if (args.length > 1) {
                        if (args.length > 2) {
                            // Case 2. 
                            try {
                                int commitID = Integer.parseInt(args[1]);
                                String fileName = args[2];
                                checkoutCommit(commitID, fileName);
                            } catch (NumberFormatException ex) {
                                // Commit id must be int. 
                               System.out.println("No commit with that id exists.");
                               return; 
                            }
                        } else {
                            // Case 1 or 3.
                            checkoutFileOrBranch(args[1]);
                        }
                    } else {
                        System.out.println("Did not enter enough arguments.");
                        return;
                    }
                } else {
                    // They said no, do not continue.
                    return;
                }
                break;
            case "branch":
                branch(input1);
                break;
            case "rm-branch":
                removeBranch(input1);
                break;
            case "reset":
                checkReset(input1); // Danger check starts it off. 
                break;
            case "merge":
                checkMerge(input1);
               break;
            case "rebase":
                checkRebase(input1);
                break;
            case "i-rebase":
                // Essentially rebase, but diff.
                // For each node it replays, it allows the user to change the commit's message or skip replaying the commit. 
                // So need to pause and prompt user for text input before continuing with each commit.
                // And then print info about the commit.
                // If continue, replay the commit and continue.
                // If skip, no replay, then ask about next one.
                    // DOESN'T MEAN FORGET ABOUT IT THO.
                // Can't skip initial or final commit of a branch. 

                checkIRebase(input1);
                break;
            default:
                System.out.println("Unrecognized command.");
                break;
        }
    }

    /* Does the actual interactive rebase stuff. */
    private static void interactiveRebase(String branchName) {

        WorldState world = getWorldState();
        HashMap<String, Integer> branchHeads = world.getBranchHeads();
        String currBranch = world.getCurrBranch();

        // Failures same as rebase. 

        // If branch with given name does not exist, error.
        if (!branchHeads.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        // If given branch name same as current branch name, error.
        if (branchName.equals(currBranch)) {
            System.out.println("Cannot rebase a branch onto itself.");
            return;
        }

        // If input branch head in history of current branch head, error.
        if (inHistory(branchName, currBranch, branchHeads)) {
            System.out.println("Already up-to-date.");
            return;
        }

        int givenBranchHead = branchHeads.get(branchName);
        int currBranchHead = branchHeads.get(currBranch);

        // Current branch in history of given branch - no replays. 
        if (inHistory(currBranch, branchName, branchHeads)) {
            noReplays(branchName, givenBranchHead, world);
            return;
        }

        int splitPoint = splitPoint(currBranch, branchName, branchHeads);
        ArrayList<Integer> commitsToCopy = idHistory(currBranch, splitPoint, branchHeads);

        HashMap<String, Integer> splitPointFiles = filesInCommit(splitPoint);
        HashMap<String, Integer> givenBranchFiles = filesInCommit(givenBranchHead);
        HashMap<String, Integer> currBranchFiles = filesInCommit(currBranchHead);

        HashMap<String, Integer> neededChanges = rebaseChanges1(splitPointFiles, givenBranchFiles, currBranchFiles);

        int firstCommitToCopy = commitsToCopy.get(0);
        int lastCommitToCopy = commitsToCopy.get(commitsToCopy.size() - 1);

        int newID = world.getNumCommits() + 1;
        int parent = givenBranchHead;
        for (int i : commitsToCopy) {
            int increaseCommitBy = interactiveChoice(newID, parent, i, neededChanges, firstCommitToCopy, lastCommitToCopy);
            if (increaseCommitBy == 1) {
                parent = newID;
            }
            newID = newID + increaseCommitBy;
        }

        // And then at end can update numcommits, branchHeads and currCommit. 
        int newNumCommits = newID - 1;
        world.newNumCommits(newNumCommits);
            // Wait which branch got changed? Should be just currentBranch. 
        branchHeads.put(currBranch, newNumCommits);
        world.updateHeadPointer(newNumCommits);
        
        // ADFHGLIDUFHGILDUHGLIDSUHGSLIDUGHSLIDUGHSLIDUHGLIAHGILASUHGILASEHULAISEUHFSLIAUHFLISEUHFLASIEUFHASE ALICE NEED TO CHANGE THINGS IN WORKING DIRECTORY TOOOOOOOO I BELIEVEEEEEEEEE
        changingWDWithCommit(newNumCommits);

        // And write back worldState. 
        writeBackWorldState(world); 
    }

    /* For interative rebase - deals with user's choice of csm. */
    private static int interactiveChoice(int newID, int parent, int commitToCopy, HashMap<String, Integer> neededChanges, int firstID, int lastID) {
        System.out.println("Currently replaying:");

        // Then print out info about the commit. 
        String commitIDLine = "Commit " + commitToCopy + ".";
        CommitWrapper currCommitWrapper = commitWrapper(commitToCopy);
        String timeLine = currCommitWrapper.getCommitTime();
        String message = currCommitWrapper.getCommitMessage();

        System.out.println(commitIDLine);
        System.out.println(timeLine);
        System.out.println(message);

        return csmChoice(newID, parent, commitToCopy, neededChanges, firstID, lastID);
    }

    /* Deals with dissiminating from the user's csm input. */
    private static int csmChoice(int newID, int parent, int commitToCopy, HashMap<String, Integer> neededChanges, int firstID, int lastID) {

        Scanner userInput = new Scanner(System.in);
        System.out.println("Would you like to (c)ontinue, (s)kip this commit, or change this commit's (m)essage?");
        String input = userInput.next();
        if (input.equals("c")) {
            // Should just be like normal rebase, right?
            copyCommits(newID, parent, commitToCopy, neededChanges);
            return 1; // Upped 1 - made the new commit. 
        } else if (input.equals("s")) {
            if (commitToCopy == firstID || commitToCopy == lastID) {
                // Cannot skip the initial or final commit of a branch - ask for input again. 
                return csmChoice(newID, parent, commitToCopy, neededChanges, firstID, lastID);
            } else {
                return 0; // Do nothing, I believe. 
            }
        } else if (input.equals("m")) {
            takeNewMessage(newID, parent, commitToCopy, neededChanges);
            return 1;
        }
        return csmChoice(newID, parent, commitToCopy, neededChanges, firstID, lastID); // User inputed something invalid - do it again. 
    }

    /* Rebase - digests user's new message. */
    private static void takeNewMessage(int newID, int parent, int commitToCopy, HashMap<String, Integer> neededChanges) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please enter a new message for this commit.");
        String message = userInput.nextLine();

        newMessageCommit(newID, parent, commitToCopy, neededChanges, message);

    }

    /* Rebase - makes new commit with the new message. */
    private static void newMessageCommit(int newID, int parent, int commitToCopy, HashMap<String, Integer> neededChanges, String newMessage) {

        // Reminiscient of copyCommits method. 

        // Make the folder. 
        createCommitFolder(newID);

        // Create CommitWrapper file.
        createCommitWrapperLocation(newID);

        // Write the commit wrapper. 
        copyWrapper(newID, commitToCopy, parent, neededChanges, newMessage);
    }

    /* Interactive rebase - Copy Commit Wrapper and write to file WITH SPECIAL USER-CHANGED MESSAGE. */
    private static void copyWrapper(int newID, int copyFromID, int parentCommit, HashMap<String, Integer> neededChanges, String newMessage) {

        CommitWrapper old = commitWrapper(copyFromID);
        CommitWrapper newVersion = new CommitWrapper(newID, old, parentCommit, neededChanges, newMessage);
        String writeTo = ".gitlet/snapshots/" + newID + "/CommitWrapper.ser";
        try {
            FileOutputStream fout = new FileOutputStream(writeTo);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(newVersion);
            oos.close();
        } catch (IOException ex) {
            System.out.println("Error - Could not copy CommitWrapper to file - interactive rebase.");
            System.exit(1);
        }
    }






    /* For rebase - no replaying commits. */
    private static void noReplays(String branchName, int givenBranchHead, WorldState world) {
        
        // Update files in working directory - just like checkout I believe. 
        checkoutFileOrBranch(branchName);

        world.updateBranchHeads(givenBranchHead);
        world.updateHeadPointer(givenBranchHead);
        writeBackWorldState(world);
    }



    /* Customary danger check for interactive rebase. */
    private static void checkIRebase(String branchName) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)");
        String input = userInput.next();
        if (input.equals("yes")) {
            interactiveRebase(branchName);
        } else {
            return;
        }
    }

    /* Actual doing of rebase stuff now. */
    private static void rebase(String branchName) {

        WorldState world = getWorldState();
        HashMap<String, Integer> branchHeads = world.getBranchHeads();
        String currBranch = world.getCurrBranch();

        // If branch with given name does not exit, error.
        if (!branchHeads.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        // If given branch name same as current branch name, error.
        if (branchName.equals(currBranch)) {
            System.out.println("Cannot rebase a branch onto itself.");
            return;
        }
        // If input branch head in history of current branch head, error. 
        if (inHistory(branchName, currBranch, branchHeads)) {
            System.out.println("Already up-to-date.");
            return;
        }

        int givenBranchHead = branchHeads.get(branchName);
        int currBranchHead = branchHeads.get(currBranch);

        // If current branch in history of given branch, just move the current branch to point to the same commit that given branch points to. 
        // ALSO HAVE TO UPDATE THE FILES.  
        if (inHistory(currBranch, branchName, branchHeads)) {
            noReplays(branchName, givenBranchHead, world);
            return;
        }

        // Make new commit? The folder, and a wrapper? And just copy the info? 


        // Okay first figure out the first commit to be replayed.
        // I'm thinking like a while loop? 
        // Or maybe a set, so I can for loop over the commits I need to copy. 

        // OKAY FIRST GET SET OF ALL COMMITS I NEED TO COPY. 
            // Find split point of the current branch and given branch.
        int splitPoint = splitPoint(currBranch, branchName, branchHeads);
            // Then go along currBranch and store IDs till get to split point. 
        ArrayList<Integer> commitsToCopy = idHistory(currBranch, splitPoint, branchHeads);

        // rUUURRRRRGGHH HAVE TO DO THE THING WHERE INHERIT ALL MODIFICATIONS IN THE GIVEN BRANCH.
            // How am I going to do this? 
            // This is very similar to merge. 

            // Maybe figure out what modifications are needed, and then pass it in to the copier?

        HashMap<String, Integer> splitPointFiles = filesInCommit(splitPoint);
        HashMap<String, Integer> givenBranchFiles = filesInCommit(givenBranchHead);
        HashMap<String, Integer> currBranchFiles = filesInCommit(currBranchHead);

            // Part 1: Similar to mergeChange1. 
        HashMap<String, Integer> neededChanges = rebaseChanges1(splitPointFiles, givenBranchFiles, currBranchFiles);


            // Part 2: Similar to mergeChange3. 
            // Jk unecessary because we just stick with current branch's copies. 


        // Each time passing in the commit to be copied, what its new number should be, and its parent (which is just --^ after each iteration). 
        int newID = world.getNumCommits() + 1; 
        int parent = givenBranchHead; // Head of given branch. 
        for (int i : commitsToCopy) {
            copyCommits(newID, parent, i, neededChanges);
            parent = newID; // Just attaching along now like a congo line. 
            newID = newID + 1;
        }


        // And then at end can update numcommits, branchHeads and currCommit. 
        int newNumCommits = newID - 1;
        world.newNumCommits(newNumCommits);
            // Wait which branch got changed? Should be just currentBranch. 
        branchHeads.put(currBranch, newNumCommits);
        world.updateHeadPointer(newNumCommits);
        
        // ADFHGLIDUFHGILDUHGLIDSUHGSLIDUGHSLIDUGHSLIDUHGLIAHGILASUHGILASEHULAISEUHFSLIAUHFLISEUHFLASIEUFHASE ALICE NEED TO CHANGE THINGS IN WORKING DIRECTORY TOOOOOOOO I BELIEVEEEEEEEEE
        changingWDWithCommit(newNumCommits);

        // And write back worldState. 
        writeBackWorldState(world); 
    }



    /* Change stuff in working directory given a specific commit ID.
    It's like checking out branch, but given the ID instead of the branch. */
    private static void changingWDWithCommit(int commit) {
        CommitWrapper commitInfo = commitWrapper(commit);
        HashMap<String, Integer> storedFiles = commitInfo.getStoredFiles();

        for (String file : storedFiles.keySet()) {
            int commitID = storedFiles.get(file);
            overwriteWorkingDirectoryFile(commitID, file);
        }
    }


    /* For rebase, gathering together all the changes needed for case1. */
    private static HashMap<String, Integer> rebaseChanges1(HashMap<String, Integer> splitPointFiles, 
        HashMap<String, Integer> givenBranchFiles, 
        HashMap<String, Integer> currBranchFiles) {

        HashMap<String, Integer> neededChanges = new HashMap<String, Integer>();

        for (String file : givenBranchFiles.keySet()) {
            // If modified since split.
            if (!givenBranchFiles.get(file).equals(splitPointFiles.get(file))) {
                // Then make sure not changed in current branch.
                if (currBranchFiles.get(file).equals(splitPointFiles.get(file))) {
                    // Get version in given branch. 
                    neededChanges.put(file, givenBranchFiles.get(file));
                }
            }
        }
        return neededChanges;
    }


    /* Copying commits for rebase. */
    private static void copyCommits(int newID, int parent, int commitToCopy, HashMap<String, Integer> neededChanges) {

        // First make the folder. 
        createCommitFolder(newID);

        // Create CommitWrapper file. 
        createCommitWrapperLocation(newID);

        // Write the commit wrapper. 
        copyWrapper(newID, commitToCopy, parent, neededChanges);

        // Only changes are commitID, isRoot(possibly), and parentCommit. 

        // If after the split point, the given branch contains modifications to files that were not modified in the current branch, then these modifications should propagate through the replayed branch. DONE. 

    }

    /* Copy Commit Wrapper and write to file. */
    private static void copyWrapper (int newID, int copyFromID, int parentCommit, HashMap<String, Integer> neededChanges) {
        CommitWrapper old = commitWrapper(copyFromID);
        CommitWrapper newVersion = new CommitWrapper(newID, old, parentCommit, neededChanges); 
        String writeTo = ".gitlet/snapshots/" + newID + "/CommitWrapper.ser";
        try {
            FileOutputStream fout = new FileOutputStream(writeTo);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(newVersion);
            oos.close();
        } catch (IOException ex) {
            System.out.println("Error - Could not copy Commit Wrapper to file.");
            System.exit(1);
        }
    }


    /* Store IDs of given branch till given point. */
    private static ArrayList<Integer> idHistory(String branchName, int stopBeforeHere, 
        HashMap<String, Integer> branchHeads) {

        int head = branchHeads.get(branchName);
        ArrayList<Integer> history = new ArrayList<Integer>();

        while (head != stopBeforeHere) {
            history.add(0, head); // Traversing backwards, so need to add to front. 
            head = parentCommit(head);
        }
        return history;

    }



    /* Check if input branch's head is in history of current branch's head. */
    private static boolean inHistory(String inputBranch, String currBranch, 
        HashMap<String, Integer> branchHeads) {

        int inputHead = branchHeads.get(inputBranch);
        int currHead = branchHeads.get(currBranch);

        if (inputHead == 0) {
            // Initial commit is in everyone's history. 
            return true;
        }
        while (currHead != 0) {
            if (currHead == inputHead) {
                return true;
            }
            currHead = parentCommit(currHead);
        }
        return false;
    }


    /* Danger check for rebase. */
    private static void checkRebase(String branchName) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)");
        String input = userInput.next();
        if (input.equals("yes")) {
            rebase(branchName);
        } else {
            return;
        }
    }


    /* Danger check for merge. */
    private static void checkMerge(String branchName) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)");
        String input = userInput.next();
        if (input.equals("yes")) {
            merge(branchName);
        } else {
            return;
        }

    }

    /* Actual doing of merge stuff now. */
    private static void merge(String givenBranch) {
        WorldState world = getWorldState();
        HashMap<String, Integer> branchHeads = world.getBranchHeads();
        String currBranch = world.getCurrBranch();

        // If branch with given name odes not exist, error.
        if (!branchHeads.containsKey(givenBranch)) {
            // Takes care of null case. 
            System.out.println("A branch with that name does not exist.");
            return;
        }

        // If attempt to merge branch with itself, error.
        if (givenBranch.equals(currBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        // Merges files from head of given branch into head of current branch. 
        int givenBranchHead = branchHeads.get(givenBranch);
        int currBranchHead = branchHeads.get(currBranch);

            // Find split point of current branch and given branch.
        int splitPointID = splitPoint(givenBranch, currBranch, branchHeads);


        HashMap<String, Integer> splitPointFiles = filesInCommit(splitPointID);
        HashMap<String, Integer> givenBranchFiles = filesInCommit(givenBranchHead);
        HashMap<String, Integer> currBranchFiles = filesInCommit(currBranchHead);

            // Any files modified in given branch (added to any of the commits along the branch and not subsequently removed) but not in current branch since split should be changed to versions in given branch. 
        mergeChange1(splitPointFiles, givenBranchFiles, currBranchFiles);


            // Any files modified in the current branch (added and not subsequently removed) but not in given branch since split --> stay same.
                // SO DO NOTTHINNNGGGG. 


            // Files modified in both branches since split (added and not subsequently removed) should stay as they are in current branch. 
            // However, version of the file from given branch should be copied into the file system with the name [oldfilename].conflicted.
        mergeChange3(splitPointFiles, givenBranchFiles, currBranchFiles);


    }

    /* Does the stuf for merge bullet point 3. */
    private static void mergeChange3 (HashMap<String, Integer> splitPointFiles, 
        HashMap<String, Integer> givenBranchFiles, 
        HashMap<String, Integer> currBranchFiles) {

        // Files modified in both branches since split (added and not subsequently removed) should stay as they are in current branch. 
        // However, version of the file from given branch should be copied into the file system with the name [oldfilename].conflicted.

        for (String file : givenBranchFiles.keySet()) {
            // First check if it's been modified since split, at least. 
            if (!givenBranchFiles.get(file).equals(splitPointFiles.get(file))) {

                // Then make sure has been changed in current branch as well. 
                if (!currBranchFiles.get(file).equals(splitPointFiles.get(file))) {
                    
                    // Copy conflicted version into file system.

                        // Create the file existence in working directory first. 
                    String wdFileLocation = createConflictFileExistenceWD(file); 
                        // Now "copy" it over. 
                    genWriteToWorkingDirectory(wdFileLocation, file, givenBranchFiles.get(file));
                }
            }
        }

    }

    /* Creates file existence in working directory - FOR CONFLICTS. */
    private static String createConflictFileExistenceWD (String originalFileName) {
        String fileLocation = originalFileName + ".conflicted";
        File newFile = new File(fileLocation);
        try {
            if (newFile.getParentFile() != null) {
                newFile.getParentFile().mkdirs();
            }
            FileWriter writer = new FileWriter(newFile);
        } catch (IOException ex) {
            System.out.println("Error - merging conflict - could not create file space in working directory.");
            System.exit(1);
        }
        return fileLocation;
    }

    /* Copies file over - more general than overwriteWorkingDirectoryFile. */
    private static void genWriteToWorkingDirectory(String wdLocation, String commitFileName, int commitID) {
        String copyFrom = ".gitlet/snapshots/" + commitID + "/" + commitFileName;
        Path inCommit = Paths.get(copyFrom);
        Path workingDirectory = Paths.get(wdLocation);
        try {
            Files.copy(inCommit, workingDirectory, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.out.println("Error - could not replace working directory file.");
            System.exit(1);
        }
    }



    /* Changes files for the first part of merge.
    /* Files modified in given branch since split but not in current branch change to versions in given branch. */
    private static void mergeChange1 (HashMap<String, Integer> splitPointFiles, 
        HashMap<String, Integer> givenBranchFiles, 
        HashMap<String, Integer> currBranchFiles) {

        for (String file : givenBranchFiles.keySet()) {
            // First check if it's been modified since split, at least. 
            if (!givenBranchFiles.get(file).equals(splitPointFiles.get(file))) {
                // Then make sure hasn't been changed in current branch.
                if (currBranchFiles.get(file).equals(splitPointFiles.get(file))) {
                    // Change to version in given branch. 
                    overwriteWorkingDirectoryFile(givenBranchFiles.get(file), file);
                }
            }
        }
    }



    /* Files stored in this commit. */
    private static HashMap<String, Integer> filesInCommit(int commitID) {
        CommitWrapper cw = commitWrapper(commitID);
        return cw.getStoredFiles();
    }



    /* Find split point (the ID of the last shared commit). */
    private static int splitPoint(String branch1, String branch2, HashMap<String, Integer> branchHeads) {

        int id1 = branchHeads.get(branch1);
        int id2 = branchHeads.get(branch2);
        if (id1 == id2) {
            return id1;
        }

        // Else, have to start looking at parents.

        // DO LIKE A SMART THING. IF 2 > 1, MOVE 2. OTHERWISE MOVE 1. IT'S GOTTA BE EQUAL AT SOME POINT. 

        int parent1 = id1;
        int parent2 = id2;
        if (parent1 > parent2) {
            parent1 = parentCommit(id1);
        } else {
            parent2 = parentCommit(id2);
        }

        while (parent1 != parent2) {
            if (parent1 > parent2) {
                // Move parent 1.
                parent1 = parentCommit(parent1);
            } else {
                // Move parent 2.
                parent2 = parentCommit(parent2);
            }
        }
        return parent1;

    }

    /* Get parent of given commit. */
    private static int parentCommit(int commitID) {
        CommitWrapper cw = commitWrapper(commitID);
        return cw.getParentCommit();
    }
         



    /* Doing danger check for reset. */
    private static void checkReset(String commitID) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)");
        String input = userInput.next();
        if (input.equals("yes")) {
            reset(commitID);
        } else {
            return;
        }
    }


    /* Actually doing reset. */
    private static void reset(String commitID) {
        // If no commit with given id exists, error.
        try {
            int commit = Integer.parseInt(commitID);
            WorldState world = getWorldState();
            int numCommits = world.getNumCommits();
            if (commit < 0 || commit > numCommits) {
                System.out.println("No commit with that id exists.");
                return;
            }

            // From this point on, JUST DO IT. NO ERRORS. 

            // Restores all files to their versions in the commit with the given id. 
                // A LITTLE REMINISCENT OF CHECKOUT, AMIRITE??

            CommitWrapper commitInfo = commitWrapper(commit);
            HashMap<String, Integer> storedFiles = commitInfo.getStoredFiles();

            for (String file : storedFiles.keySet()) {
                int whereFileIs = storedFiles.get(file);
                overwriteWorkingDirectoryFile(whereFileIs, file);
            }

            // Also moves the current branch's head to that commit node. 
            world.updateBranchHeads(commit);
            world.updateHeadPointer(commit);

            // DON'T FORGET TO WRITE BACK WORLD STATE. 
            writeBackWorldState(world);

        } catch (NumberFormatException ex) { 
            // Inputed string not an int. 
            System.out.println("No commit with that id exists.");
            return;
        } 

    }



    /* Deletes branch with given name. */
    private static void removeBranch(String branchName) {
        if (branchName == null) {
            System.out.println("Did not enter enough arguments.");
            return;
        }

        WorldState world = getWorldState();
        HashMap<String, Integer> branchHeads = world.getBranchHeads();

        // If try to remove the branch you're currently on, abort. 
        if (world.getCurrBranch().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }

        // If branch with given name does not exist, abort.
        if (!branchHeads.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        // Just delete the head pointer associated with the branch. Not like, deleting commits or anything.
            // So should only change branchHeads. 
        branchHeads.remove(branchName);

        // DON'T FORGET TO WRITE BACK WORLD. 
        writeBackWorldState(world);
    }

    private static void branch(String newBranchName) {
        // Creating branch just gives new head pointer.
        // At any given time, 1 head pointer = current active head pointer.
        // Can switch the currently active head pointer with checkout.
        // Whenever commit = add a new commit in front of the CURRENTLY ACTIVE HEAD POINTER, even if one is already there. --> BRANCHES
        if (newBranchName == null) {
            System.out.println("Did not enter enough arguments.");
            return;
        }

        WorldState world = getWorldState();
        HashMap<String, Integer> branchHeads = world.getBranchHeads();
        // If branch name already exists, error.
        if (branchHeads.containsKey(newBranchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        // Creates a new branch - just a name of a head pointer in the commit graph. 
            // DOESN'T IMMEDIATELY SWITCH TO NEARLY CREATED BRANCH. 
            // The branch points to, like the same thing your current branch does.

            // So only need to update branchHeads. 
        world.createNewBranch(newBranchName);

        // You updated WorldState - DON'T FORGET TO WRITE BACK.
        writeBackWorldState(world);

    }

    private static void checkoutCommit(int commitID, String fileName) {

        WorldState world = getWorldState();
        int numCommits = world.getNumCommits();
        if (commitID < 0 || commitID > numCommits) {
            // No commit with given ID exists. 
            System.out.println("No commit with that id exists.");
            return;
        }

        CommitWrapper commitInfo = commitWrapper(commitID);
        HashMap<String, Integer> storedFiles = commitInfo.getStoredFiles();
        if (storedFiles.containsKey(fileName)) {
            int commitNum = storedFiles.get(fileName);
            overwriteWorkingDirectoryFile(commitNum, fileName);
        } else {
            // File does not exist in the commit. 
            System.out.println("File does not exist in that commit.");
            return;
        }
    }

    private static void checkoutFileOrBranch(String thingToCheckout) {
        // Branch > File. 

        WorldState world = getWorldState();
        HashMap<String, Integer> branches = world.getBranchHeads();
        if (branches.containsKey(thingToCheckout)) {
            // It's a branch. 

            // Check if branch is current branch - error.
            if (thingToCheckout.equals(world.getCurrBranch())) {
                System.out.println("No need to checkout the current branch.");
                return;
            }
            // 3: Restores all files in working directory to their versions in the commit at the head of the given branch.

            int branchHead = branches.get(thingToCheckout);
            CommitWrapper commitInfo = commitWrapper(branchHead);
            HashMap<String, Integer> storedFiles = commitInfo.getStoredFiles();

            for (String file : storedFiles.keySet()) {
                int commitID = storedFiles.get(file);
                overwriteWorkingDirectoryFile(commitID, file);
            }

            // Given branch now current branch. 
            world.updateCurrBranch(thingToCheckout);

            // Should update currCommit as well, to reflect this change in position. 
                // Should just be head of new current branch. 
            world.updateHeadPointer(branchHead);

            // Write back world, of course. 
            writeBackWorldState(world);

        } else {
            // It's a file. 

            // 1: Restores the file to its state at the commit at the head of current branch. 

            String currBranch = world.getCurrBranch();
            int branchHead2 = branches.get(currBranch);
            CommitWrapper commitInfo2 = commitWrapper(branchHead2);
            HashMap<String, Integer> storedFiles2 = commitInfo2.getStoredFiles();

            if (storedFiles2.keySet().contains(thingToCheckout)) {
                int commitID2 = storedFiles2.get(thingToCheckout);
                overwriteWorkingDirectoryFile(commitID2, thingToCheckout);
            } else {
                // File does not exist in the previous commit OR No branch with that name exists.
                System.out.println("File does not exist in the most recent commit, or no such branch exists.");
                return;
            }
        }
    }

    private static void overwriteWorkingDirectoryFile(int commitID, String fileName) {
        String inCommitFile = ".gitlet/snapshots/" + commitID + "/" + fileName;
        Path inCommit = Paths.get(inCommitFile);
        Path workingDirectory = Paths.get(fileName);
        try {
            Files.copy(inCommit, workingDirectory, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.out.println("Error - Could not replace working directory file.");
            System.exit(1);
        }
    }

    private static void status() {
        printBranches();
        // Displays what files have been staged or marked for removal.
        printStagedFiles();
        printRemovalFiles();


    }

    /* Displays what branches currently exist, and marks current branch with *. */
    private static void printBranches() {
        WorldState world = getWorldState();
        Set<String> branches = world.getBranchHeads().keySet();
        String currBranch = world.getCurrBranch();
        System.out.println("=== Branches ===");
        for (String branchName : branches) {
            if (branchName.equals(currBranch)) {
                System.out.println("*" + branchName);
            } else {
                System.out.println(branchName);
            }
        }
        System.out.println();
    }

    private static void printStagedFiles() {
        Staging stage = getStaging();
        Set<String> addedFiles = stage.getFilesToAdd();
        System.out.println("=== Staged Files ===");
        for (String file : addedFiles) {
            System.out.println(file);
        }
        System.out.println();
    }

    private static void printRemovalFiles() {
        Staging stage = getStaging();
        Set<String> removalFiles = stage.getFilesToRemove();
        System.out.println("=== Files Marked for Removal ===");
        for (String file : removalFiles) {
            System.out.println(file);
        }
        System.out.println();
    }



    /* Print out IDs with this commit message. */
    private static void find(String message) {
        WorldState world = getWorldState();
        HashMap<String, ArrayList<Integer>> commitsByMessage = world.getCommitsByMessage();
        if (commitsByMessage.containsKey(message)) {
            ArrayList<Integer> commits = commitsByMessage.get(message);
            for (Integer id : commits) {
                System.out.println(id);
            }

        } else {
            System.out.println("Found no commit with that message.");
        } 
    }



    /* Print ALL commits EVER. */
    private static void globalLog() {
        // The best way I think to do this is go 0, 1, 2, 3....till numCommits. 
            // numCommits starts at 0, so don't need to -1.
        WorldState world = getWorldState();
        Integer numCommits = world.getNumCommits();
        for (int i = 0; i <= numCommits; i = i + 1) {
            String commitIDLine = "Commit " + i + ".";
            CommitWrapper currCommitWrapper = commitWrapper(i);
            String timeLine = currCommitWrapper.getCommitTime();
            String message = currCommitWrapper.getCommitMessage();
            printing(commitIDLine, timeLine, message);
        }
    }

    private static void log() {
        // Starts at current head pointer FOR CURRENT BRANCH. 
        WorldState world = getWorldState();
        int currHead = world.getHeadOfCurrBranch();
        String commitIDLine = "Commit " + currHead + ".";
        CommitWrapper currCommitWrapper = commitWrapper(currHead);
        String timeLine = currCommitWrapper.getCommitTime();
        String message = currCommitWrapper.getCommitMessage();
        printing(commitIDLine, timeLine, message);

        // THEN GO TILL THE END.
        Integer parentID = currCommitWrapper.getParentCommit();
        while (parentID != null) {
            commitIDLine = "Commit " + parentID + ".";
            currCommitWrapper = commitWrapper(parentID);
            timeLine = currCommitWrapper.getCommitTime();
            message = currCommitWrapper.getCommitMessage();
            printing(commitIDLine, timeLine, message);

            parentID = currCommitWrapper.getParentCommit();
        }

    }


    private static void printing(String commitID, String time, String message) {
        // Display commit ID, time, and message. Don't forget the ==== seperation & empty line in between.
        System.out.println("====");
        System.out.println(commitID);
        System.out.println(time);
        System.out.println(message);
        System.out.println();
    }


    private static void checkCommit(String commitMessage) {
        if (commitMessage != null) {
            commit(commitMessage);
        } else {
            System.out.println("Please enter a commit message.");
            return;
        }
    }



    private static void commit(String commitMessage) {
        /* If no files staged or marked for removal, abort. */
        if (!stuffToAdd() && !stuffToRemove()) {
            System.out.println("No changes added to tbe commit.");
            return;
        }


        /* Create new commit folder. */
            // First figure out what commit number this should be.
            // Should be WorldState's numCommits + 1.
        WorldState worldState = getWorldState();
        int commitID = worldState.getNumCommits() + 1;
        createCommitFolder(commitID);



        /* Write the commitWrapper. */
        
            // Create CommitWrapper file first. 
        createCommitWrapperLocation(commitID);

        // DO ALL THE WRITING OF THE COMMIT WRAPPER YO. 
        writeCommitWrapperToFile(commitID, commitMessage);
        
        /* Put the files into the commit folder - INCLUDED IN CREATION OF WRAPPER NO WORRIES. */


        /* DON'T FORGET TO CLEAR THE STAGING INFO. */
        Staging stage = getStaging();
        stage.emptyStagingInfo();
        // Write stage back to Staging.ser.
        writeBackStaging(stage);



        /* Update WorldState last!!!!!! */

        // Move head pointer (currCommit). 
        worldState.updateHeadPointer(commitID);
        // Update numCommits.
        worldState.updateNumCommits();
        // Update branchHeads. 
        worldState.updateBranchHeads(commitID);
        // Update commitsByMessage. 
        worldState.updateCommitMessages(commitMessage, commitID);
        // Now write worldState back into its .ser file. 
        writeBackWorldState(worldState);



        /* This is a test - commitWrapper.ser. */


        /*try {
            FileInputStream fin = new FileInputStream(".gitlet/snapshots/" + commitID + "/CommitWrapper.ser");
            ObjectInputStream ois = new ObjectInputStream(fin);
            CommitWrapper check = (CommitWrapper) ois.readObject();
            ois.close();
            System.out.println("Time of Commit " + commitID + ": " + check.getCommitTime());
            System.out.println("Parent of Commit " + commitID + ": " + check.getParentCommit());

            HashMap<String, Integer> checkStoredFiles = check.getStoredFiles();
            for (String file : checkStoredFiles.keySet()) {
                System.out.println("Tracking " + file + " in ID " + checkStoredFiles.get(file));
            }

            // Make sure Staging.ser is emptied. 
            FileInputStream finStaging = new FileInputStream(".gitlet/Staging.ser");
            ObjectInputStream oisStaging = new ObjectInputStream(finStaging);
            Staging emptyStage = (Staging) oisStaging.readObject();
            oisStaging.close();
            System.out.println("Files to add is empty: " + emptyStage.getFilesToAdd().isEmpty());

            // Make sure WorldState is good. 
            FileInputStream finWorld = new FileInputStream(".gitlet/WorldState.ser");
            ObjectInputStream oisWorld = new ObjectInputStream(finWorld);
            WorldState currWorld = (WorldState) oisWorld.readObject();
            oisWorld.close();
            System.out.println("Head pointer: " + currWorld.getCurrCommit());
            HashMap<String, Integer> branchHeads = currWorld.getBranchHeads();
            System.out.println("Branch head of master: " + branchHeads.get("master"));
            
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Testing commit went wrong.");
            System.exit(1);
        }*/
    }

    /* Checks if .gitlet can be initialized. */
    private static void checkInitialize() {
        
        // What would I do w/o StackOverflow. 
        File f = new File(".gitlet/");
        if (f.exists() && f.isDirectory()) {
            // Already a thing exists in the current directory.
            String one = "A gitlet version control system ";
            String two = "already exists in the current directory.";
            System.out.println(one + two); 
            // Darn character limit.
        } else {
            initialize(f);
        }
    }


    private static void initialize(File newGitlet) {
        // Make new gitlet directory.
        newGitlet.mkdir(); 

        /* Make WorldState.ser. */
        // Thanks to Japheth and mkyong.com for the major help. HOPE THIS WORKS.
        WorldState worldState = new WorldState();
        try {
            FileOutputStream foutWorldState = new FileOutputStream(".gitlet/WorldState.ser");
            ObjectOutputStream oosWorldState = new ObjectOutputStream(foutWorldState);
            oosWorldState.writeObject(worldState);
            oosWorldState.close();
        } catch (IOException ex) {
            System.out.println("Error - initialize - could not write WorldState.ser");
            System.exit(1);
        }

        /* Make Staging.ser. */
        Staging stagingInfo = new Staging();
        try {
            FileOutputStream foutStaging = new FileOutputStream(".gitlet/Staging.ser");
            ObjectOutputStream oosStaging = new ObjectOutputStream(foutStaging);
            oosStaging.writeObject(stagingInfo);
            oosStaging.close();
        } catch (IOException ex2) {
            System.out.println("Error - initialize - could not write Staging.ser");
            System.exit(1);
        }

        /* Make Snapshots folder. */
        File newSnapshots = new File(".gitlet/snapshots");
        newSnapshots.mkdir();

        /* Now make Commit0 in snapshots folder. */
        createCommitFolder(0);
        
        /*String zeroSerWrapper = ".gitlet/snapshots/0/CommitWrapper.ser";
        File zeroFileSerWrapper = new File(zeroSerWrapper);
        try {
            zeroFileSerWrapper.createNewFile();
        } catch (IOException ex3) {
            System.out.println("Initialize - could not create CommitWrapper file.");
            System.exit(1);
        }*/
        createCommitWrapperLocation(0);

        /*try {
            CommitWrapper commitInfo = new CommitWrapper(0);
            FileOutputStream foutCommitting = new FileOutputStream(".gitlet/snapshots/0/CommitWrapper.ser");
            ObjectOutputStream oosCommitting = new ObjectOutputStream(foutCommitting);
            oosCommitting.writeObject(commitInfo);
            oosCommitting.close();
        } catch (IOException ex4) {
            System.out.println("Initialize - could not write CommitWrapper object.");
            System.exit(1);
        }*/
        writeCommitWrapperToFile(0, "initial commit");


        /* Test. */ 


        /*try {
            FileInputStream fin = new FileInputStream(".gitlet/snapshots/0/CommitWrapper.ser");
            ObjectInputStream ois = new ObjectInputStream(fin);
            CommitWrapper initialCommit = (CommitWrapper) ois.readObject();
            ois.close();
            System.out.println("Commit 0 time: " + initialCommit.getCommitTime());
            System.out.println("Commit 0 parent: " + initialCommit.getParentCommit());

            HashMap<String, Integer> storedFiles = initialCommit.getStoredFiles();
            if (storedFiles.isEmpty()) {
                System.out.println("Good, no stored files in initial commit.");
            } else {
                System.out.println("BAD THERE SHOULD BE NO STORED FILES IN INITIAL COMMIT.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Testing initial went wrong.");
            System.exit(1);
        }*/

    }

    private static void checkAdd(String addFile) {
        // Can only add 1 file at a time.
        // Command line arguments split on space, I believe.
        if (addFile != null) {
            add(addFile);
        } else {
            System.out.println("Did not enter enough arguments.");
            return;
        }
    }

    

    private static void add(String fileName) {
        // Have to add the string to Staging.ser - should be it.

        // File does not exist:
        if (!(new File(fileName).exists())) {
            System.out.println("File does not exist.");
            return;
        }

        // So here on, guaranteed file exists. 
        try {

            /* File not been modified since last commit: */
                // IF IT HASN'T PREVIOUSLY BEEN TRACKED, IT'S OKAY
            if (lastCommitTracks(fileName) && (!modifiedSinceLastCommit(fileName))) {
                System.out.println("File has not been modified since the last commit.");
                return;
            }

            // Unmark if marked for removal.
            Staging stagingInfo = getStaging();
            if (stagingInfo.removeContains(fileName)) {
                stagingInfo.removeFromRemove(fileName);
            }

            /* Add the string to Staging.ser. */
            stagingInfo.addFile(fileName);
            
            // Saving it back. 
            FileOutputStream fout = new FileOutputStream(".gitlet/Staging.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(stagingInfo);
            oos.close();


            /* A test.  - COMMENT OUT LATER ALICE. */
            /*FileInputStream fin2 = new FileInputStream(".gitlet/Staging.ser");
            ObjectInputStream ois2 = new ObjectInputStream(fin2);
            Staging stagingInfo2 = (Staging) ois2.readObject();
            ois2.close();
            System.out.println(fileName + " has been added: " + stagingInfo2.addContains(fileName));*/

        } catch (IOException ex) {
            System.out.println("Error - exception in add.");
            System.exit(1);
        }

    }

    private static void checkRemove(String removeFile) {
        if (removeFile != null) {
            remove(removeFile);
        } else {
            System.out.println("Did not enter enough arguments.");
            return;
        }
    }

    private static void remove(String fileName) {
        /* If file neither added nor included in previous commit, print error. */
        if (!lastCommitTracks(fileName) && !hasAdded(fileName)) {
            System.out.println("No reason to remove the file.");
            return;
        }

        // Mark file for removal - will not be inherited.
        markToRemove(fileName);

        // If file was staged, unstage it.
        if (hasAdded(fileName)) {
            unstage(fileName);
        }        
        
    }

    private static boolean lastCommitTracks(String fileName) {
        // Assumes file exists.
        CommitWrapper commitInfo = lastCommitWrapper();
        return commitInfo.isTracking(fileName);
    }

    private static Integer lastCommit() {
        try {
            FileInputStream fin = new FileInputStream(".gitlet/WorldState.ser");
            ObjectInputStream ois = new ObjectInputStream(fin);
            WorldState worldState = (WorldState) ois.readObject();
            ois.close();

            return worldState.getCurrCommit();

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error - WorldState.ser could not be read.");
            ex.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private static CommitWrapper lastCommitWrapper() {
        int currCommit = lastCommit();
        return commitWrapper(currCommit);
    }

    /* Gets Commit Wrapper of given commit ID. */
    private static CommitWrapper commitWrapper(int commitID) {
        // Go into the folder, pull out its CommitWrapper. 
        try {
            /*System.out.println(".gitlet/snapshots/" + currCommit + "/CommitWrapper.ser");*/
            FileInputStream fin2 = new FileInputStream(".gitlet/snapshots/" + commitID + "/CommitWrapper.ser");
            ObjectInputStream ois2 = new ObjectInputStream(fin2);
            CommitWrapper commitInfo = (CommitWrapper) ois2.readObject();
            ois2.close();

            return commitInfo;
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex);
            System.out.println("Commit wrapper could not be read.");
            System.exit(1);
        }
        return null;
    }

    /* Assuming last commit does track it!!!!! */
    private static Boolean modifiedSinceLastCommit(String fileName) {
        CommitWrapper lastCommit = lastCommitWrapper();
        /*FileInputStream currFile = new FileInputStream(fileName);
        DataInputStream currData = new DataInputStream(currFile);
        System.out.println("currFile: " + fileName);
        Byte curr = currData.readByte();*/

        /*Path currFile = Paths.get(fileName);
        byte[] curr = Files.readAllBytes(currFile);*/

        // Figure out where the last modified file is. 
        HashMap<String, Integer> fileLocationInfo = lastCommit.getStoredFiles();
        int folderNum = fileLocationInfo.get(fileName);
        String filePath = ".gitlet/snapshots/" + folderNum + "/" + fileName;

        // Now read it in. 
        /*FileInputStream lastCommitFile = new FileInputStream(filePath);
        DataInputStream lastCommitData = new DataInputStream(lastCommitFile);
        System.out.println("last commit file path: " + filePath);
        Byte last = lastCommitData.readByte();*/

        /*Path lastCommitFile = Paths.get(filePath);
        byte[] last = Files.readAllBytes(lastCommitFile);*/

        // Now compare. 
        return !filesEqual(fileName, filePath);
    }

    /* Thanks to java2s.com and Apache, I believe. */
    private static Boolean filesEqual(String path1, String path2) {
        try {
            FileInputStream f1 = new FileInputStream(path1);
            BufferedReader file1 = new BufferedReader(new InputStreamReader(f1, "UTF-8"));

            FileInputStream f2 = new FileInputStream(path2);
            BufferedReader file2 = new BufferedReader(new InputStreamReader(f2, "UTF-8"));

            int ch = file1.read();
            while (-1 != ch) {
                int ch2 = file2.read();
                if (ch != ch2) {
                    return false;
                }
                ch = file1.read();
            }
            int ch2 = file2.read();
            return ch2 == -1;
        } catch (IOException ex) {
            System.out.println("Error - could not compare files.");
            System.exit(1);
        }
        return null;
    }



    /* Return Staging object. */
    private static Staging getStaging() {
        try {
            FileInputStream fin = new FileInputStream(".gitlet/Staging.ser");
            ObjectInputStream ois = new ObjectInputStream(fin);
            Staging staging = (Staging) ois.readObject();
            ois.close();
            return staging;
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error - could not read in Staging.ser.");
            System.exit(1);
        }
        return null;
    }

    /* Write Staging back. */
    private static void writeBackStaging(Staging updatedStage) {
        try {
            FileOutputStream fout = new FileOutputStream(".gitlet/Staging.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(updatedStage);
            oos.close();
        } catch (IOException ex) {
            System.out.println("Error - could not write back Staging.ser.");
            System.exit(1);
        }
    }

    /* Unstages a file. */ 
    private static void unstage(String fileName) {
        Staging stage = getStaging();
        stage.removeFromAdd(fileName);
        writeBackStaging(stage);
    }

    /* Marks file as to be removed. */
    private static void markToRemove(String fileName) {
        Staging stage = getStaging();
        stage.removeFile(fileName);
        // Don't forget to rewrite Staging.ser back.
        writeBackStaging(stage);
    }

    /* Has this particular file been added? */
    private static boolean hasAdded(String fileName) {
        Staging staging = getStaging();
        Set<String> addFiles = staging.getFilesToAdd();
        return addFiles.contains(fileName); 
    }

    /* Is there stuff to be added in Staging? */
    private static boolean stuffToAdd() {
        Staging staging = getStaging();
        return staging.hasFilesToAdd();
    }

    /* Is there stuff to be removed in Staging? */
    private static boolean stuffToRemove() {
        Staging staging = getStaging();
        return staging.hasFilesToRemove();
    }

    /* Create new commit folder. */
    private static void createCommitFolder(int commitID) {
        File newCommit = new File(".gitlet/snapshots/" + commitID);
        newCommit.mkdir();
    }

    private static WorldState getWorldState() {
        try {
            FileInputStream fin = new FileInputStream(".gitlet/WorldState.ser");
            ObjectInputStream ois = new ObjectInputStream(fin);
            WorldState worldState = (WorldState) ois.readObject();
            ois.close();
            return worldState;
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error - could not read in WorldState.");
            System.exit(1);
        }
        return null;
    }

    private static void writeBackWorldState(WorldState worldState) {
        try {
            FileOutputStream fout = new FileOutputStream(".gitlet/WorldState.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(worldState);
            oos.close();
        } catch (IOException ex) {
            System.out.println("Error - could not write back WorldState.");
            System.exit(1);
        }
    }

    private static void createCommitWrapperLocation(int commitID) {
        String path = ".gitlet/snapshots/" + commitID + "/CommitWrapper.ser";
        File commitWrapper = new File(path);
        try {
            commitWrapper.createNewFile();
        } catch (IOException ex) {
            System.out.println("Error - could not create CommitWrapper.");
            System.exit(1);
        }
    }

    private static void writeCommitWrapperToFile(int commitID, String message) {
        CommitWrapper commitWrapper = new CommitWrapper(commitID, message);
        String fileLocation = ".gitlet/snapshots/" + commitID + "/CommitWrapper.ser";
        try {
            FileOutputStream fout = new FileOutputStream(fileLocation);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(commitWrapper);
            oos.close();
        } catch (IOException ex) {
            System.out.println("Error - could not write commit wrapper to file.");
            System.exit(1);
        }
    }
}
