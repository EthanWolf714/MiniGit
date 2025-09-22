package test.java;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.nio.file.Path;
import miniGitPkg.MiniGit; 

public class MiniGitTest {

    @TempDir
    Path tempDir;
    Path tempFile;



    @Test
    void testInit() throws IOException{
        //Act
        MiniGit.init(tempDir);

        //Assert - check in temp directory
        assertTrue(Files.exists(tempDir.resolve(".minigit")));
        assertTrue(Files.exists(tempDir.resolve(".minigit/objects")));
        assertTrue(Files.exists(tempDir.resolve(".minigit/HEAD")));
    }

    @Test
    void testAdd() throws IOException{
        //initialize repo
        MiniGit.init();

        //create test file and add to directory
        Files.write(Paths.get("test.txt"), "hello world".getBytes());
        System.out.println("Created test file: " + Files.exists(Paths.get("test.txt")));

        MiniGit.add("test.txt");

        System.out.println("Index exists: " + Files.exists(Paths.get(".minigit/index")));

        //test if index file was altered or updated
        assertTrue(Files.exists(Paths.get(".minigit/index")));

        //clean files
        deleteDirectory(Paths.get(".minigit"));

        Files.deleteIfExists(Paths.get("test.txt"));
        
        
    }

    @Test
    void testCommit() throws IOException{
        MiniGit.init();
        Files.write(Paths.get("test.txt"), "hello world".getBytes());
        System.out.println("Created test file: " + Files.exists(Paths.get("test.txt")));

        MiniGit.add("test.txt");

        MiniGit.commit("commit test file");

        String indexContext = Files.readString(Paths.get(".minigit/index"));

        assertTrue(indexContext.trim().isEmpty());

        deleteDirectory(Paths.get(".minigit"));

        Files.deleteIfExists(Paths.get("test.txt"));
        
    }

    @Test 
    void log() throws IOException{
        MiniGit.init();
        Files.write(Paths.get("test.txt"), "hello world".getBytes());
        System.out.println("Created test file: " + Files.exists(Paths.get("test.txt")));

        MiniGit.add("test.txt");

        MiniGit.commit("commit test file");

        MiniGit.log();

        deleteDirectory(Paths.get(".minigit"));

        Files.deleteIfExists(Paths.get("test.txt"));
    }

    @AfterEach
    void tearDown() throws IOException{
        try {
            deleteDirectory(Paths.get(".minigit"));
            Files.deleteIfExists(Paths.get("test.txt"));
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    public static void deleteDirectory(Path directory) throws IOException {
    if (Files.exists(directory)) {
        Files.walk(directory)
             .sorted(Comparator.reverseOrder()) // Delete files before directories
             .map(Path::toFile)
             .forEach(File::delete);
    }
}


    
}
