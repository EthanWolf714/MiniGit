package miniGitPkg;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.time.Instant;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;


public class MiniGit {
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("Please specify a command");
            return;
        }

        switch (args[0]) {
            case "init":
                init();
                break;
            case "add":
                if (args.length < 2) {
                    System.out.println("Please provide a file to add");
                    break;
                }
                add(args[1]);
                break;
            case "commit":
                if (args.length < 2) {
                    System.out.println("Please provide a commit message");
                    break;
                }
                commit(args[1]);
                break;
            case "log":
                log();
                break;
            default:
                System.out.println("Unknown command: " + args[0]);
        }

		
	}

    //method for test purposes
    public static void init(Path baseDir) throws IOException {
        Path minigitDir = baseDir.resolve(".minigit");
        Files.createDirectories(minigitDir);
        Files.createDirectories(minigitDir.resolve("objects"));
        Files.createFile(minigitDir.resolve("HEAD"));
    }

    public static void init() throws IOException{
         String initDirectoryPath = ".minigit";
            // Create a File object representing the directory
            File directory = new File(initDirectoryPath);

            //check if directory already exists, if not then create directory with supporting files
            if(directory.exists() && directory.isDirectory()){
                System.out.println(".minigit directory is present");

            }else{
                
                System.out.println(".minigit directory does not exist");
                File initDirectory = new File(".minigit");
                boolean initCreated = initDirectory.mkdir();

                if(initCreated){
                    System.out.print(".minigit directory created!");
                }else{
                    System.out.println("Failed to create directory. It may already exist");
                }

                File objectsDir = new File(".minigit/objects");
                boolean objectsCreated = objectsDir.mkdir();

                if(objectsCreated){
                    System.out.print("objects directory created at!");
                }else{
                    System.out.println("Failed to create directory. It may already exist at!");
                }
                
                File headFile = new File(".minigit/HEAD");
                boolean headFileCreated = headFile.createNewFile();
                if(headFileCreated){
                    System.out.println("HEAD file created");
                }else{
                    System.out.println("File already exists");
                }

            }
    }
   
    public static void add(String filename) throws IOException{

            String fileName = filename;
            //create file
            File file = new File(fileName);
            if(!file.exists()){
                System.out.println("This file doesn't exist");
                return;
            }
            //check if is a file and not a directory
            if(!file.isFile()){
                System.out.println(fileName + " is not a file");
                return;
            }

            //get the files path and read all bytes data into an array of bytes
            Path filePath = Paths.get(fileName);
            byte[] fileBytes = Files.readAllBytes(filePath);

            //get message isntance for SHA-1 algorithm
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                System.out.println("SHA-1 algorithm not found");
                return;
            }
            //feed the bytes into the hash
            byte[] hash = md.digest(fileBytes);

            String hashedString;
            try (Formatter formatter = new Formatter()) {
                //loop through each byte in hash and convert to hexedecimal
                for(byte b : hash){
                    formatter.format("%02x", b);
                }
                //convert hex to string
                hashedString = formatter.toString();
            }
            
            //write the files to the staging area 
            Path objectPath = Paths.get(".minigit/objects/" + hashedString);
            Files.write(objectPath, fileBytes);

            //print staging info
            String stagingInfo = fileName + " " + hashedString + " \n";
            Files.write(Paths.get(".minigit/index"), stagingInfo.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            //confirmation message.
            System.out.println("Added " + fileName + " to staging area");
    }

    public static void commit(String message) throws IOException{
            File indexFile = new File(".minigit/index");

            //check if file exists
            if(!indexFile.exists()){
                System.out.println("nothing to commit");
                return;
            }


            String indexContent = Files.readString(Paths.get(".minigit/index"));
            //check if file is empty
            if(indexContent.trim().isEmpty()){
                System.out.println("Nothing to commit");
                return;
            }

            //build commit string
            Instant timestamp = Instant.now();
            String commitString = "MESSAGE:" + message + "\n" +
                     "TIMESTAMP:" + timestamp + "\n" + 
                     "FILES:\n" + indexContent;

            byte[] commitBytes = commitString.getBytes();

            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                System.out.println("SHA-1 algorithm not found");
                return;
            }
            byte[] hash = md.digest(commitBytes);

            String hashedCommitString;
            try (Formatter formatter = new Formatter()) {
                //loop through each byte in hash and convert to hexedecimal
                for(byte b : hash){
                    formatter.format("%02x", b);
                }
                //convert hex to string
                hashedCommitString = formatter.toString();
            }


            File commitFile = new File(".minigit/objects/" + hashedCommitString);
            
            Path commitPath = Paths.get(".minigit/objects/" + hashedCommitString);
            Files.write(commitPath,commitBytes );

            System.out.println("Committed  " + hashedCommitString);
            

            //overwrite the head file 
            Files.write(Paths.get(".minigit/HEAD"), hashedCommitString.getBytes());


            //clear staging area
            PrintWriter writer = new PrintWriter(".minigit/index");
            writer.print("");
            writer.close();
    }

    public static void log() throws IOException{
        //Read HEAD to get current commit hash
        String currentCommit = Files.readString(Paths.get(".minigit/HEAD"));
        //read commit objects
        String commitData = Files.readString(Paths.get(".minigit/objects/" + currentCommit));

        // Parse and print the commit info
        System.out.println("Commit: " + currentCommit);
        System.out.println("Data: " + commitData);
    

    }



}
