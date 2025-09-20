import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat.Style;

public class MiniGit {
	public static void main(String[] args) {


        if (args.length == 0){
            System.out.println("Please specify a command");
            return;
        }

        if(args[0].equals("init")){
            String initDirectoryPath = ".minigit";
            File directory = new File(initDirectoryPath);

            if(directory.exists() && directory.isDirectory()){
                System.out.println(".minigit directory is present");

            }else{
                System.out.println(".minigit directory does not exist");
                String directoryName = ".minigit";
                String currentDirectory = System.getProperty(".minigit");

                String directoryPath = currentDirectory + File.separator + directoryName;

                File newDirectory = new File(directoryPath);

                boolean directoryCreated = newDirectory.mkdir();

                if(directoryCreated){
                    System.out.print("minigit directory created at:" + directoryPath);
                }else{
                    System.out.println("Failed to create directory. It may already exist at: " + directoryPath);
                }

            }
        }
		
	}



}
