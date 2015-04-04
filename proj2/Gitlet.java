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
import java.lang.ClassNotFoundException;
import java.io.DataInputStream;
import java.util.ArrayList;

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

        private static final long serialVersionUID = 1L; // Apparently do this because force the version number, so won't InvalidClassException when try to deseriazlie it.

        private WorldState() {
            // Probably only create a new WorldState at very beginning.
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


        private void updateHeadPointer(int commitID) {
            this.currCommit = commitID;
        }

        private void updateNumCommits() {
            this.numCommits = this.numCommits + 1;
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
            branchHeads.put(newBranchName, currCommit);
        }

        private HashMap<String, Integer> getBranchHeads() {
            return this.branchHeads;
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
            // Thanks a million to mkyong.com. Hope this works.
            /*if (!isRoot) {
                // You create a CommitWrapper when you make a commit - in main method of Gitlet. So you're in the working directory. 
                try {
                    FileInputStream fin = new FileInputStream(".gitlet/WorldState.ser");
                    ObjectInputStream ois = new ObjectInputStream(fin); 
                    // --^ These two - IOException.
                    WorldState worldState = (WorldState) ois.readObject(); 
                    // --^ ClassNotFoundException.
                    ois.close();
                    parentCommit = worldState.getCurrCommit(); 
                    // Shouldn't have updated yet.
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("Exception in creating Commit Wrapper.");
                    System.exit(1);
                    // This shouldn't happen - WorldState should be the first thing created. 
                }
            } else {
                // It's for commit 0, has no parent.
                parentCommit = null;
            }*/

            this.parentCommit = calculateParentCommit(this.isRoot);



            /* Create map of files in this commit folder. */
            // Take it from Staging.ser, yeah??
            // CONTENTS FROM TIME OF COMMIT (not add) ARE RECORDED



            // First, copy all parent's files. 
            this.storedFiles = parentsFiles(this.isRoot);

            // Then take out stuff in filesToRemove. 
            removeFiles(this.storedFiles, this.isRoot);

            // Take working directory versions of filesToAdd, stick in folder. Update the map while at it.
            addFilesToFolder(this.commitID, this.isRoot, this.storedFiles);



            // Take all the parent's remembered files.
          /*  if (!isRoot) {
                // Only has a parent if it's not the root. 
                try {
                    String parentFolderLocation = ".gitlet/snapshots/" + parentCommit + "/CommitWrapper.ser";
                    FileInputStream finParent = new FileInputStream(parentFolderLocation);
                    ObjectInputStream oisParent = new ObjectInputStream(finParent);
                    CommitWrapper parent = (CommitWrapper) oisParent.readObject();
                    oisParent.close();

                    // According to StackOverflow, b/c String & Integer are immutable, I can do this:
                    this.storedFiles = new HashMap<String, Integer>(parent.storedFiles);
                } catch (IOException | ClassNotFoundException ex2) {
                    System.out.println("Exception in CommitWrapper - couldn't get parent's commitWrapper.");
                    System.exit(1);
                }
                

                // Commit 0, the root, contains no files. 
                try {
                    FileInputStream finStaging = new FileInputStream(".gitlet/Staging.ser");
                    ObjectInputStream oisStaging = new ObjectInputStream(finStaging);
                    Staging stage = (Staging) oisStaging.readObject();
                    oisStaging.close();

                    // Take out the stuff in filesToRemove.
                    if (!stage.filesToRemove.isEmpty()) {
                        for (String fRemove : stage.filesToRemove) {
                            if (stage.filesToAdd.contains(fRemove)) {
                                // Unstage it. 
                                stage.removeFile(fRemove);
                            } else {
                                // Do not inherit it. 
                                this.storedFiles.remove(fRemove);
                            }
                        }
                    }

                    // Then take the working directory versions of filesToAdd, stick in folder.
                
                    if (!stage.filesToAdd.isEmpty()) {
                        // If there are files to add to the actual folder. 
                        for (String f : stage.filesToAdd) {
                            // Create the space in the folder first.
                            String newFileLocation = ".gitlet/snapshots/" + this.commitID + "/" + f;
                            File newFile = new File(newFileLocation);
                            newFile.createNewFile();
                            // Now copy it over - thanks to examples.javacodegeeks.com.
                            FileChannel inputChannel = null;
                            FileChannel outputChannel = null;
                            try {
                                inputChannel = new FileInputStream(f).getChannel();
                                outputChannel = new FileOutputStream(newFileLocation).getChannel();
                                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                            } finally {
                                inputChannel.close();
                                outputChannel.close();
                            }

                            // Now update the map.
                            this.storedFiles.put(f, this.commitID);
                        }
                    }
      
                    // Empty Staging.ser.
                    stage.emptyStagingInfo();

                } catch (IOException | ClassNotFoundException ex3) {
                    System.out.println("Exception in CommitWrapper - couldn't get staging info.");
                    System.exit(1);
                }

                

                
            }*/

        }
        // FINISHED CONSTRUCTOR


        /* Add working directory files to folder, but also update map in CommitWrapper object. */
        private void addFilesToFolder(int commitID, boolean isRoot, HashMap<String, Integer> storedFiles) {
            if (!isRoot) {
                Staging stage = getStaging();

                if (stage.hasFilesToAdd()) {
                    for (String file : stage.getFilesToAdd()) {

                        // Create space in folder first.
                        String filePath = createFileExistence(commitID, file);

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
                            System.out.println("Could not copy files to commit folder.");
                            System.exit(1);
                        } 

                        // Update map. 
                        storedFiles.put(file, commitID);
                    }
                }
            }
        }

        private String createFileExistence(int commitID, String file) {
            String fileLocation = ".gitlet/snapshots/" + commitID + "/" + file;
            File newFile = new File(fileLocation);
            try {
                newFile.createNewFile();
            } catch (IOException ex) {
                System.out.println("Could not create file space in commit folders.");
                ex.printStackTrace();
                System.exit(1);
            }
            return fileLocation;
        }

        private void removeFiles(HashMap<String, Integer> fileMap, boolean isRoot) {
            // Commit 0 contains no files.
            if (!isRoot) {
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

        private HashMap<String, Integer> parentsFiles(boolean isRoot) {
            if (!isRoot) {
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

        private Integer calculateParentCommit(boolean isRoot) {
            if (!isRoot) {
                // Calculate parent from WorldState.
                // DO NOT UPDATE WORLDSTATE TILL VERY END. 
                // So World State's current commit should be the parent. 
                return lastCommit();
            } 
            // If you're the root, you have no parent.
            return null;
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
        if (args.length > 0) {
            command = args[0];
        } else {
            System.out.println("No command given.");
            return;
        }

        switch (command) {
            case "init":
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
                break;

            case "add":
                // Can only add 1 file at a time.
                // Command line arguments split on space, I believe.
                String addFile = null;
                if (args.length > 1) {
                    addFile = args[1];
                } else {
                    System.out.println("No file stated to add.");
                    return;
                }
                add(addFile);
                break;

            case "commit":
                 // Commit must have a non-blank message.
                String commitMessage = null;
                if (args.length > 1) {
                    commitMessage = args[1];
                } else {
                    System.out.println("Please enter a commit message.");
                    return;
                }
                commit(commitMessage);
                break;
            case "remove":
                String removeFile = null;
                if (args.length > 1) {
                    removeFile = args[1];
                } else {
                    System.out.println("No file stated to remove.");
                    return;
                }
                remove(removeFile);
                break;
            case "log":
                log();
                break;
            case "global-log":


               
        }
    }

    private static void log() {
        // Starts at current head pointer. 
        int currHead = lastCommit();
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
            System.out.println("Initialize - could not write WorldState.ser");
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
            System.out.println("Initialize - could not write Staging.ser");
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
            FileInputStream fin2 = new FileInputStream(".gitlet/Staging.ser");
            ObjectInputStream ois2 = new ObjectInputStream(fin2);
            Staging stagingInfo2 = (Staging) ois2.readObject();
            ois2.close();
            System.out.println(fileName + " has been added: " + stagingInfo2.addContains(fileName));

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Exception in add.");
            System.exit(1);
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
            System.out.println("WorldState.ser could not be read.");
            ex.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private static CommitWrapper lastCommitWrapper() {
        int currCommit = lastCommit();
        return commitWrapper(currCommit);
    }

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
        try {
            FileInputStream currFile = new FileInputStream(fileName);
            DataInputStream currData = new DataInputStream(currFile);
            Byte curr = currData.readByte();

            // Figure out where the last modified file is. 
            HashMap<String, Integer> fileLocationInfo = lastCommit.getStoredFiles();
            int folderNum = fileLocationInfo.get(fileName);
            String filePath = ".gitlet/snapshots/" + folderNum + "/" + fileName;

            // Now read it in. 
            FileInputStream lastCommitFile = new FileInputStream(filePath);
            DataInputStream lastCommitData = new DataInputStream(lastCommitFile);
            Byte last = lastCommitData.readByte();

            // Now compare. 
            System.out.println("Modified: " + !curr.equals(last));
            return !curr.equals(last);

        } catch (IOException ex) {
            System.out.println("Commit - files could not be read and compared.");
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
            System.out.println("Could not read in Staging.ser.");
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
            System.out.println("Could not write back Staging.ser.");
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
            System.out.println("Could not read in WorldState.");
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
            System.out.println("Could not write back WorldState.");
            System.exit(1);
        }
    }

    private static void createCommitWrapperLocation(int commitID) {
        String path = ".gitlet/snapshots/" + commitID + "/CommitWrapper.ser";
        File commitWrapper = new File(path);
        try {
            commitWrapper.createNewFile();
        } catch (IOException ex) {
            System.out.println("Could not create CommitWrapper.");
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
            System.out.println("Could not write commit wrapper to file.");
            System.exit(1);
        }
    }
}