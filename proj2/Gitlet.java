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

public class Gitlet {

    /* Creating the WorldState object (WorldState.ser). */
    private static class WorldState implements Serializable {

        private int currCommit;
        private int numCommits;
        private String currBranch;
        private HashMap<String, Integer> branchHeads; 
        // --^ Branch name to head ID #. 
        private HashMap<String, Integer> commitsByMessage; 
        // --^ Commit message to ID #. 

        private WorldState() {
            // Probably only create a new WorldState at very beginning.
            currCommit = 0;
            numCommits = 0;

            currBranch = "master";

            branchHeads = new HashMap<String, Integer>();
            branchHeads.put("master", 0);

            commitsByMessage = new HashMap<String, Integer>();
            commitsByMessage.put("initial commit", 0);
        }

        public Integer getCurrCommit() {
            return this.currCommit;
        }

        public Integer getNumCommits() {
            return this.numCommits;
        }

        private void updateNewCommit(String commitMessage) {
            this.numCommits = this.numCommits + 1;
            this.currCommit = this.numCommits;

            branchHeads.put(currBranch, this.currCommit);

            commitsByMessage.put(commitMessage, this.currCommit);
        }

        private void createNewBranch(String newBranchName) {
            // DOESN'T SWITCH TO NEW BRANCH.
            branchHeads.put(newBranchName, currCommit);
        }
    }



    /* Creating the Staging info object (Staging.ser). */
    private static class Staging implements Serializable {

        private Set<String> filesToAdd;
        private Set<String> filesToRemove;

        private Staging() {
            // Only gonna create a new Staging at very beginning, I think. 
            filesToAdd = new HashSet<String>();
            filesToRemove = new HashSet<String>();
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
    }


    /* Creating the Serializable class for each commit. */
    private static class CommitWrapper implements Serializable {

        private Integer commitID;
        private String commitTime;
        private boolean isRoot; 
        // --^ If true, no parent.
        private Integer parentCommit; 
        // --^ So I can use null for commit 0.
        private HashMap<String, Integer> storedFiles;

        private CommitWrapper(Integer commitNum) {
            // Thanks to StackOverflow for showing me how to get the time.

            commitID = commitNum;
            if (commitNum == 0) {
                isRoot = true;
            } else {
                isRoot = false;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            commitTime = dateFormat.format(cal.getTime());

            /* Grab parent commit ID from the file. */
            // Thanks a million to mkyong.com. Hope this works.
            if (!isRoot) {
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
            } 

            /* Create map of files in this commit folder. */
            // Take it from Staging.ser, yeah??
            // CONTENTS FROM TIME OF COMMIT (not add) ARE RECORDED

            // Take all the parent's remembered files.
            if (!isRoot) {
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

                            /* Now update the map. */
                            this.storedFiles.put(f, this.commitID);
                        }
                    }
      
                    // Empty Staging.ser.
                    stage.emptyStagingInfo();

                } catch (IOException | ClassNotFoundException ex3) {
                    System.out.println("Exception in CommitWrapper - couldn't get staging info.");
                    System.exit(1);
                }

                

                
            }

        }
        // FINISHED CONSTRUCTOR 

        public Integer getCommitID() {
            return this.commitID;
        }

        public String getCommitTime() {
            return this.commitTime;
        }

        public Integer getParentCommit() {
            return this.parentCommit;
        }

        public HashMap<String, Integer> getStoredFiles() {
            return this.storedFiles;
        }

        /* I don't know if CommitWrappers need to be able to do other stuff. FOR LATER I GUESS. */
    }




    public static void main(String[] args) {
        
        String command = null; 
        // --^ Initialize, else java mad. 
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
        File commit0 = new File(".gitlet/snapshots/0");
        commit0.mkdir();
        String zeroSerWrapper = ".gitlet/snapshots/0/CommitWrapper.ser";
        File zeroFileSerWrapper = new File(zeroSerWrapper);
        try {
            zeroFileSerWrapper.createNewFile();
        } catch (IOException ex3) {
            System.out.println("Initialize - could not create CommitWrapper file.");
            System.exit(1);
        }

        try {
            CommitWrapper commitInfo = new CommitWrapper(0);
            FileOutputStream foutCommitting = new FileOutputStream(".gitlet/snapshots/0/CommitWrapper.ser");
            ObjectOutputStream oosCommitting = new ObjectOutputStream(foutCommitting);
            oosCommitting.writeObject(commitInfo);
            oosCommitting.close();
        } catch (IOException ex4) {
            System.out.println("Initialize - could not write CommitWrapper object.");
            System.exit(1);
        }
    }
}