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
        private HashMap<String, ArrayList<Integer>> commitsByMessage; 

        // Force the version number, to avoid InvalidClassException when deserializing.
        private static final long serialVersionUID = 1L; 

        private WorldState() {
            // World State only created at initialization. 
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

        public int getCurrCommit() {
            return this.currCommit;
        }

        public int getNumCommits() {
            return this.numCommits;
        }

        public int getHeadOfCurrBranch() {
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

        /* For use in rebase. */
        private void newNumCommits(int newSum) {
            this.numCommits = newSum;
        }

        private void updateBranchHeads(int commitID) {
            branchHeads.put(currBranch, commitID);
        }

        private void updateCommitMessages(String commitMessage, int commitID) {
            ArrayList<Integer> arrayList;
            if (this.commitsByMessage.containsKey(commitMessage)) {
                // Has mapping already, so add to the existing array.
                arrayList = this.commitsByMessage.get(commitMessage);
            } else {
                // Create new mapping. 
                arrayList = new ArrayList<Integer>();
            }
            arrayList.add(commitID);
            commitsByMessage.put(commitMessage, arrayList); 
        }

        private void createNewBranch(String newBranchName) {
            // New branch points to same thing current branch does.
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



    /* Staging info object (Staging.ser). */
    private static class Staging implements Serializable {

        private Set<String> filesToAdd;
        private Set<String> filesToRemove;

        private static final long serialVersionUID = 2L;

        private Staging() {
            // Only create a new Staging at very beginning. 
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

        private int commitID;
        private String commitTime;
        private String commitMessage;
        private boolean isRoot;
        private int parentCommit;
        private HashMap<String, Integer> storedFiles; 

        private static final long serialVersionUID = 3L;

        /* Special constructor for new message in interactive rebase. */
        private CommitWrapper(int newID, CommitWrapper toCopy, int parent, 
            HashMap<String, Integer> neededChanges, String newMessage) {

            this.commitMessage = newMessage;
            this.commitID = newID;

            // Initial commit won't be from rebase. 
            this.isRoot = false; 

            this.commitTime = toCopy.getCommitTime();
            this.parentCommit = parent;

            this.storedFiles = toCopy.getStoredFiles();
            // Must add in the changes. 
            this.storedFiles.putAll(neededChanges);
        }


        /* Constructor to copy Commit Wrapper of another ID - used in rebase. */
        private CommitWrapper(int newID, CommitWrapper toCopy, int parent, 
            HashMap<String, Integer> neededChanges) {

            // Copy everything except commitID, isRoot, and parentCommit. 

            this.commitMessage = toCopy.getCommitMessage();
            this.commitID = newID;

            // Initial commit won't be from rebase. 
            this.isRoot = false; 

            this.commitTime = toCopy.getCommitTime();
            this.parentCommit = parent;

            this.storedFiles = toCopy.getStoredFiles();
            // Need to add in the changes. 
            this.storedFiles.putAll(neededChanges);
        }


        /* Normal constructor. */
        private CommitWrapper(int commitNum, String message) {
            this.commitMessage = message;
            this.commitID = commitNum;

            if (commitNum == 0) {
                isRoot = true;
            } else {
                isRoot = false;
            }

            this.commitTime = calculateCommitTime();

            this.parentCommit = calculateParentCommit(this.isRoot);

            /* Create map of files in this commit folder. */

            // First, copy all parent's files. 
            this.storedFiles = parentsFiles(this.isRoot);

            // Then take out stuff in filesToRemove. 
            removeFiles(this.storedFiles, this.isRoot);

            // Then take working directory versions of filesToAdd and put in folder. 
            // Update the map also.
            addFilesToFolder(this.commitID, this.isRoot, this.storedFiles);

        }


        /* Add working directory files to folder, and also update map in CommitWrapper object. */
        private void addFilesToFolder(int commit, boolean root, HashMap<String, Integer> stored) {
            if (!root) {
                Staging stage = getStaging();

                if (stage.hasFilesToAdd()) {
                    for (String file : stage.getFilesToAdd()) {

                        // Create space in folder first.
                        String filePath = createFileExistence(commit, file);

                        // Copy it over - thanks to examples.javacodegeeks.com.
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
            // File may be buried in folders, so create directories to maintain this file name.
            // Thanks to Stack Overflow.
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
                // Parent is the "curr commit" being looked at in World State. 
                CommitWrapper parentCommitWrapper = lastCommitWrapper();
                return new HashMap<String, Integer>(parentCommitWrapper.getStoredFiles());
            }
            return new HashMap<String, Integer>();
        }


        private String calculateCommitTime() {
            // Thanks to StackOverflow.
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            commitTime = dateFormat.format(cal.getTime());
            return commitTime;
        }

        private int calculateParentCommit(boolean root) {
            if (!root) { 
                // WorldState not updated till very end, so  current commit should be the parent. 
                return lastCommit();
            } 
            // If root, no parent.  
            return -1;
        }
        
        public int getCommitID() {
            return this.commitID;
        }

        public String getCommitTime() {
            return this.commitTime;
        }

        public String getCommitMessage() {
            return this.commitMessage;
        }

        public int getParentCommit() {
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
        
        String command = null; 
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
                checkAdd(input1);
                break;
            case "commit":
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
                if (input1 != null) {
                    find(input1);
                } else {
                    System.out.println("Did not enter enough arguments.");
                }
                break;
            case "status":
                status();
                break;
            case "checkout":
                // Thanks to homeandlearn.co.uk for user input stuff. 
                checkCheckout(input1, input2);
                break;
            case "branch":
                branch(input1);
                break;
            case "rm-branch":
                removeBranch(input1);
                break;
            case "reset":
                checkReset(input1); 
                break;
            case "merge":
                checkMerge(input1);
                break;
            case "rebase":
                checkRebase(input1);
                break;
            case "i-rebase":
                checkIRebase(input1);
                break;
            default:
                System.out.println("Unrecognized command.");
                break;
        }
    }

    /* Danger check for checkout. */
    private static void checkCheckout(String input1, String input2) {
        Scanner userInput = new Scanner(System.in);
        String w1 = "Warning: the command you entered may alter the files in your working ";
        String w2 = "directory. Uncommitted changes may be lost. ";
        String w3 = "Are you sure you want to continue? (yes/no)";
        System.out.println(w1 + w2 + w3);
        String input = userInput.nextLine();
        if (input.equals("yes")) {
            if (input1 != null) {
                if (input2 != null) {
                    // Case 2. 
                    try {
                        int commitID = Integer.parseInt(input1);
                        String fileName = input2;
                        checkoutCommit(commitID, fileName);
                    } catch (NumberFormatException ex) {
                        // Commit id must be int. 
                        System.out.println("No commit with that id exists.");
                        return; 
                    }
                } else {
                    // Case 1 or 3.
                    checkoutFileOrBranch(input1);
                }
            } else {
                System.out.println("Did not enter enough arguments.");
                return;
            }
        } else {
            // Said no, do not continue.
            return;
        }
    }

    /* Does the actual interactive rebase stuff. */
    private static void interactiveRebase(String branchName) {

        WorldState world = getWorldState();
        HashMap<String, Integer> branchHeads = world.getBranchHeads();
        String currBranch = world.getCurrBranch();

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

        HashMap<String, Integer> neededChanges = rebaseChanges1(splitPointFiles, 
            givenBranchFiles, currBranchFiles);

        int firstCommitToCopy = commitsToCopy.get(0);
        int lastCommitToCopy = commitsToCopy.get(commitsToCopy.size() - 1);

        int newID = world.getNumCommits() + 1;
        int parent = givenBranchHead;
        for (int i : commitsToCopy) {
            int increaseCommitBy = interactiveChoice(newID, parent, i, neededChanges, 
                firstCommitToCopy, lastCommitToCopy);
            if (increaseCommitBy == 1) {
                parent = newID;
            }
            newID = newID + increaseCommitBy;
        }

        // At end, update numcommits, branchHeads and currCommit. 
        int newNumCommits = newID - 1;
        world.newNumCommits(newNumCommits);

        branchHeads.put(currBranch, newNumCommits);
        world.updateHeadPointer(newNumCommits);
        
        // Change things in working directory. 
        changingWDWithCommit(newNumCommits);

        // Write back WorldState. 
        writeBackWorldState(world); 
    }

    /* For interative rebase - deals with user's choice of csm. */
    private static int interactiveChoice(int newID, int parent, int commitToCopy, 
        HashMap<String, Integer> neededChanges, int firstID, int lastID) {

        System.out.println("Currently replaying:");

        String commitIDLine = "Commit " + commitToCopy + ".";
        CommitWrapper currCommitWrapper = commitWrapper(commitToCopy);
        String timeLine = currCommitWrapper.getCommitTime();
        String message = currCommitWrapper.getCommitMessage();

        System.out.println(commitIDLine);
        System.out.println(timeLine);
        System.out.println(message);

        return csmChoice(newID, parent, commitToCopy, neededChanges, firstID, lastID);
    }


    /* Delegates according to user's csm choice. 
    Also returns number of commits replayed. */
    private static int csmChoice(int newID, int parent, int commitToCopy, 
        HashMap<String, Integer> neededChanges, int firstID, int lastID) {

        Scanner userInput = new Scanner(System.in);
        String c1 = "Would you like to (c)ontinue, (s)kip this commit, ";
        String c2 = "or change this commit's (m)essage?";
        System.out.println(c1 + c2);
        String input = userInput.next();

        if (input.equals("c")) {
            // Should just be like normal rebase.
            copyCommits(newID, parent, commitToCopy, neededChanges);
            return 1;  
        } else if (input.equals("s")) {
            if (commitToCopy == firstID || commitToCopy == lastID) {
                // Cannot skip the initial or final commit of a branch. 
                return csmChoice(newID, parent, commitToCopy, neededChanges, firstID, lastID);
            } else {
                return 0; 
            }
        } else if (input.equals("m")) {
            takeNewMessage(newID, parent, commitToCopy, neededChanges);
            return 1;
        }
        // User inputed something invalid, try again.
        return csmChoice(newID, parent, commitToCopy, neededChanges, firstID, lastID); 
    }


    /* Rebase - digests user's new message. */
    private static void takeNewMessage(int newID, int parent, 
        int commitToCopy, HashMap<String, Integer> neededChanges) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please enter a new message for this commit.");
        String message = userInput.nextLine();

        newMessageCommit(newID, parent, commitToCopy, neededChanges, message);
    }


    /* Rebase - makes new commit with the new message. */
    private static void newMessageCommit(int newID, int parent, int commitToCopy, 
        HashMap<String, Integer> neededChanges, String newMessage) {

        // Reminiscient of copyCommits method. 

        createCommitFolder(newID);

        createCommitWrapperLocation(newID);

        // Write the commit wrapper. 
        copyWrapper(newID, commitToCopy, parent, neededChanges, newMessage);
    }

    /* I-rebase - Copy Commit Wrapper and write to file with user's new message. */
    private static void copyWrapper(int newID, int copyFromID, int parentCommit, 
        HashMap<String, Integer> neededChanges, String newMessage) {

        CommitWrapper old = commitWrapper(copyFromID);
        CommitWrapper nv = new CommitWrapper(newID, old, parentCommit, neededChanges, newMessage);
        String writeTo = ".gitlet/snapshots/" + newID + "/CommitWrapper.ser";
        try {
            FileOutputStream fout = new FileOutputStream(writeTo);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(nv);
            oos.close();
        } catch (IOException ex) {
            System.out.println("Error - Could not copy CommitWrapper to file - i-rebase.");
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
        String w1 = "Warning: the command you entered may alter the files in your working ";
        String w2 = "directory. Uncommitted changes may be lost. ";
        String w3 = "Are you sure you want to continue? (yes/no)";
        System.out.println(w1 + w2 + w3);
        String input = userInput.next();
        if (input.equals("yes")) {
            interactiveRebase(branchName);
        } else {
            return;
        }
    }

    /* Actual doing of rebase stuff. */
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

        if (inHistory(currBranch, branchName, branchHeads)) {
            // Move current branch to point at same commit.
            // And update files in working directory.
            noReplays(branchName, givenBranchHead, world);
            return;
        }
 
        // Find split point of the current branch and given branch.
        int splitPoint = splitPoint(currBranch, branchName, branchHeads);
        // Go along currBranch and store IDs till split point.
        ArrayList<Integer> commitsToCopy = idHistory(currBranch, splitPoint, branchHeads);


        HashMap<String, Integer> spf = filesInCommit(splitPoint);
        HashMap<String, Integer> gbf = filesInCommit(givenBranchHead);
        HashMap<String, Integer> cbf = filesInCommit(currBranchHead);


        HashMap<String, Integer> neededChanges = rebaseChanges1(spf, gbf, cbf);

 
        int newID = world.getNumCommits() + 1; 
        int parent = givenBranchHead; 

        for (int i : commitsToCopy) {
            copyCommits(newID, parent, i, neededChanges);
            parent = newID; 
            newID = newID + 1;
        }


        // At end, update numcommits, branchHeads and currCommit. 
        int newNumCommits = newID - 1;
        world.newNumCommits(newNumCommits);

        branchHeads.put(currBranch, newNumCommits);

        world.updateHeadPointer(newNumCommits);
        
        // Change things in working directory.
        changingWDWithCommit(newNumCommits);

        // Write back worldState. 
        writeBackWorldState(world); 
    }



    /* Change working directory given a specific commit ID.
    Similar to checking out branch, but given the ID . */
    private static void changingWDWithCommit(int commit) {
        CommitWrapper commitInfo = commitWrapper(commit);
        HashMap<String, Integer> storedFiles = commitInfo.getStoredFiles();

        for (String file : storedFiles.keySet()) {
            int commitID = storedFiles.get(file);
            overwriteWorkingDirectoryFile(commitID, file);
        }
    }


    /* For rebase, gathering together all the changes needed for case1. */
    private static HashMap<String, Integer> rebaseChanges1(HashMap<String, Integer> 
        splitPointFiles, 
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
    private static void copyCommits(int newID, int parent, int commitToCopy, 
        HashMap<String, Integer> neededChanges) {
 
        createCommitFolder(newID);
        createCommitWrapperLocation(newID);
        copyWrapper(newID, commitToCopy, parent, neededChanges);
    }


    /* Copy Commit Wrapper and write to file. */
    private static void copyWrapper(int newID, int copyFromID, int parentCommit, 
        HashMap<String, Integer> neededChanges) {

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
            // Traversing backwards, so need to add to front. 
            history.add(0, head); 
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
        String w1 = "Warning: the command you entered may alter the files in your working ";
        String w2 = "directory. Uncommitted changes may be lost. ";
        String w3 = "Are you sure you want to continue? (yes/no)";
        System.out.println(w1 + w2 + w3);
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
        String w1 = "Warning: the command you entered may alter the files in your working ";
        String w2 = "directory. Uncommitted changes may be lost. ";
        String w3 = "Are you sure you want to continue? (yes/no)";
        System.out.println(w1 + w2 + w3);
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

        // Case 1:
        mergeChange1(splitPointFiles, givenBranchFiles, currBranchFiles);

        // Case 2: do nothing.

        // Case 3: 
        mergeChange3(splitPointFiles, givenBranchFiles, currBranchFiles);
    }



    /* Does merge bullet point 3. */
    private static void mergeChange3(HashMap<String, Integer> splitPointFiles, 
        HashMap<String, Integer> givenBranchFiles, 
        HashMap<String, Integer> currBranchFiles) {

        for (String file : givenBranchFiles.keySet()) {
            // First check if it's been modified since split.
            if (!givenBranchFiles.get(file).equals(splitPointFiles.get(file))) {

                // Then make sure has been changed in current branch as well. 
                if (!currBranchFiles.get(file).equals(splitPointFiles.get(file))) {
                    
                    // Copy conflicted version into file system.
                    String wdFileLocation = createConflictFileExistenceWD(file);
                    genWriteToWorkingDirectory(wdFileLocation, file, givenBranchFiles.get(file));
                }
            }
        }
    }


    /* Creates file existence in working directory - for conflicts. */
    private static String createConflictFileExistenceWD(String originalFileName) {
        String fileLocation = originalFileName + ".conflicted";
        File newFile = new File(fileLocation);
        try {
            if (newFile.getParentFile() != null) {
                newFile.getParentFile().mkdirs();
            }
            FileWriter writer = new FileWriter(newFile);
        } catch (IOException ex) {
            System.out.println("Error - could not create file space in working directory.");
            System.exit(1);
        }
        return fileLocation;
    }


    /* Copies file over - more general than overwriteWorkingDirectoryFile. */
    private static void genWriteToWorkingDirectory(String wdLocation, 
        String commitFileName, int commitID) {
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


    /* Changes files for the first part of merge.*/
    private static void mergeChange1(HashMap<String, Integer> splitPointFiles, 
        HashMap<String, Integer> givenBranchFiles, 
        HashMap<String, Integer> currBranchFiles) {

        for (String file : givenBranchFiles.keySet()) {

            if (!givenBranchFiles.get(file).equals(splitPointFiles.get(file))) {

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
    private static int splitPoint(String branch1, String branch2, 
        HashMap<String, Integer> branchHeads) {

        int id1 = branchHeads.get(branch1);
        int id2 = branchHeads.get(branch2);
        if (id1 == id2) {
            return id1;
        }

        // Else, look at parents.

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
        String w1 = "Warning: the command you entered may alter the files in your working ";
        String w2 = "directory. Uncommitted changes may be lost. ";
        String w3 = "Are you sure you want to continue? (yes/no)";
        System.out.println(w1 + w2 + w3);
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

            // Restores all files to their versions in the commit with the given id. 
                // Similar to checkout.

            CommitWrapper commitInfo = commitWrapper(commit);
            HashMap<String, Integer> storedFiles = commitInfo.getStoredFiles();

            for (String file : storedFiles.keySet()) {
                int whereFileIs = storedFiles.get(file);
                overwriteWorkingDirectoryFile(whereFileIs, file);
            }

            // Move current branch's head to that commit node.
            world.updateBranchHeads(commit);
            world.updateHeadPointer(commit);

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

        branchHeads.remove(branchName);
        writeBackWorldState(world);
    }

    private static void branch(String newBranchName) {
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
        world.createNewBranch(newBranchName);

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
            System.out.println("File does not exist in that commit.");
            return;
        }
    }

    private static void checkoutFileOrBranch(String thingToCheckout) {

        WorldState world = getWorldState();
        HashMap<String, Integer> branches = world.getBranchHeads();
        if (branches.containsKey(thingToCheckout)) {
            // It's a branch. 

            if (thingToCheckout.equals(world.getCurrBranch())) {
                System.out.println("No need to checkout the current branch.");
                return;
            }
            // 3: Restores all files in working directory to their versions 
            // in the commit at the head of the given branch.

            int branchHead = branches.get(thingToCheckout);
            CommitWrapper commitInfo = commitWrapper(branchHead);
            HashMap<String, Integer> storedFiles = commitInfo.getStoredFiles();

            for (String file : storedFiles.keySet()) {
                int commitID = storedFiles.get(file);
                overwriteWorkingDirectoryFile(commitID, file);
            }

            // Given branch now current branch. 
            world.updateCurrBranch(thingToCheckout);
 
            world.updateHeadPointer(branchHead);

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
                String f1 = "File does not exist in the most recent commit, ";
                String f2 = "or no such branch exists.";
                System.out.println(f1 + f2);
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
            for (int id : commits) {
                System.out.println(id);
            }

        } else {
            System.out.println("Found no commit with that message.");
        } 
    }



    /* Print all commits ever. */
    private static void globalLog() {
        // Go 0, 1, 2, 3....till numCommits. 
        WorldState world = getWorldState();
        int numCommits = world.getNumCommits();
        for (int i = 0; i <= numCommits; i = i + 1) {
            String commitIDLine = "Commit " + i + ".";
            CommitWrapper currCommitWrapper = commitWrapper(i);
            String timeLine = currCommitWrapper.getCommitTime();
            String message = currCommitWrapper.getCommitMessage();
            printing(commitIDLine, timeLine, message);
        }
    }


    private static void log() {
        // Starts at head pointer for current branch. 
        WorldState world = getWorldState();
        int currHead = world.getHeadOfCurrBranch();
        String commitIDLine = "Commit " + currHead + ".";
        CommitWrapper currCommitWrapper = commitWrapper(currHead);
        String timeLine = currCommitWrapper.getCommitTime();
        String message = currCommitWrapper.getCommitMessage();
        printing(commitIDLine, timeLine, message);

        // Then go until the very end.
        int parentID = currCommitWrapper.getParentCommit();
        while (parentID >= 0) {
            commitIDLine = "Commit " + parentID + ".";
            currCommitWrapper = commitWrapper(parentID);
            timeLine = currCommitWrapper.getCommitTime();
            message = currCommitWrapper.getCommitMessage();
            printing(commitIDLine, timeLine, message);

            parentID = currCommitWrapper.getParentCommit();
        }

    }


    private static void printing(String commitID, String time, String message) {
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

        // Commit ID should be WorldState's numCommits + 1.
        WorldState worldState = getWorldState();
        int commitID = worldState.getNumCommits() + 1;
        createCommitFolder(commitID);


        /* Write the commitWrapper. */
        
        // Create CommitWrapper file first. 
        createCommitWrapperLocation(commitID);

        writeCommitWrapperToFile(commitID, commitMessage);

        Staging stage = getStaging();
        stage.emptyStagingInfo();
        writeBackStaging(stage);



        /* Update WorldState last. */

        // Move head pointer (currCommit). 
        worldState.updateHeadPointer(commitID);
        // Update numCommits.
        worldState.updateNumCommits();
        // Update branchHeads. 
        worldState.updateBranchHeads(commitID);
        // Update commitsByMessage. 
        worldState.updateCommitMessages(commitMessage, commitID);
        
        writeBackWorldState(worldState);
    }


    /* Checks if .gitlet can be initialized. */
    private static void checkInitialize() {
        
        // Thank you StackOverflow. 
        File f = new File(".gitlet/");
        if (f.exists() && f.isDirectory()) {
            String one = "A gitlet version control system ";
            String two = "already exists in the current directory.";
            System.out.println(one + two); 
        } else {
            initialize(f);
        }
    }


    private static void initialize(File newGitlet) {
        newGitlet.mkdir(); 

        /* Make WorldState.ser. */
        // Thanks to Japheth and mkyong.com for the major help.
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

        /* Make Commit0 in snapshots folder. */
        createCommitFolder(0);
        
        createCommitWrapperLocation(0);

        writeCommitWrapperToFile(0, "initial commit");
    }


    private static void checkAdd(String addFile) {
        if (addFile != null) {
            add(addFile);
        } else {
            System.out.println("Did not enter enough arguments.");
            return;
        }
    }


    private static void add(String fileName) {
        // Just add the string to Staging.ser.

        if (!(new File(fileName).exists())) {
            System.out.println("File does not exist.");
            return;
        }

        try {

            /* File not been modified since last commit: */
            // If wasn't previously tracked though, it's okay.
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
        /* If file neither added nor included in previous commit, error. */
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
        CommitWrapper commitInfo = lastCommitWrapper();
        return commitInfo.isTracking(fileName);
    }


    private static int lastCommit() {
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
        return -1;
    }


    private static CommitWrapper lastCommitWrapper() {
        int currCommit = lastCommit();
        return commitWrapper(currCommit);
    }


    /* Gets Commit Wrapper of given commit ID. */
    private static CommitWrapper commitWrapper(int commitID) {
        try {
            String s1 = ".gitlet/snapshots/" + commitID + "/CommitWrapper.ser";
            FileInputStream fin2 = new FileInputStream(s1);
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


    /* Use after being sure last commit does track it. */
    private static Boolean modifiedSinceLastCommit(String fileName) {

        CommitWrapper lastCommit = lastCommitWrapper();

        // Figure out where the last modified file is. 
        HashMap<String, Integer> fileLocationInfo = lastCommit.getStoredFiles();
        int folderNum = fileLocationInfo.get(fileName);
        String filePath = ".gitlet/snapshots/" + folderNum + "/" + fileName;

        // Now compare. 
        return !filesEqual(fileName, filePath);
    }


    /* Thanks to java2s.com. */
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
