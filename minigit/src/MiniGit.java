import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.text.NumberFormat.Style;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class MiniGit {
    public static void main(String[] args) throws IOException {


        if (args.length == 0){
            System.out.println("Please specify a command");
            return;
        }

        if(args[0].equals("init")){
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

        if(args[0].equals("add") ){
            if(args.length < 2){
                System.out.println("Please provide a file to add");
                return;
            }

            String fileName = args[1];
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
		
	}



}
