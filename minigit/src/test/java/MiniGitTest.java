package test.java;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import miniGitPkg.MiniGit; 

public class MiniGitTest {

    @TempDir
    Path tempDir;



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
    void testAdd(){

    }


    
}
