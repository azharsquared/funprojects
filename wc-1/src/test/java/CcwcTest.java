/*
 * This Java source file was generated by the Gradle 'init' task.
 */


import org.as2.Ccwc;
import org.as2.Result;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CcwcTest {
    
    @Test
    void TestTextFileCountChars() throws URISyntaxException {

        URL resource = CcwcTest.class.getResource("text.txt");
        File file = Paths.get(resource.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();

        String[] args = { "-c", absolutePath };
        
        var classUnderTest = new Ccwc();
        var cmd = new CommandLine(classUnderTest);
        cmd.execute(args);
        Result result = cmd.getExecutionResult();
        assertEquals(-1, result.countLines, "line count is 0");
        assertEquals(-1, result.countWords, "word count is 0");
        assertEquals(341836, result.countChars, "byte count is 341836");
    }

    @Test
    void TestTextFileCountLines() throws URISyntaxException {

        URL resource = CcwcTest.class.getResource("text.txt");
        File file = Paths.get(resource.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();

        String[] args = { "-l", absolutePath };
        
        var classUnderTest = new Ccwc();
        var cmd = new CommandLine(classUnderTest);
        cmd.execute(args);
        Result result = cmd.getExecutionResult();
        assertEquals(7137, result.countLines, "line count is 7137");
        assertEquals(-1, result.countWords, "word count is 0");
        assertEquals(-1, result.countChars, "byte count is 0");
    }

    @Test
    void TestTextFileCountWords() throws URISyntaxException {

        URL resource = CcwcTest.class.getResource("text.txt");
        File file = Paths.get(resource.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();

        String[] args = { "-w", absolutePath };
        
        var classUnderTest = new Ccwc();
        var cmd = new CommandLine(classUnderTest);
        cmd.execute(args);
        Result result = cmd.getExecutionResult();
        assertEquals(-1, result.countLines, "line count is 0");
        assertEquals(58159, result.countWords, "word count is 58159");
        assertEquals(-1, result.countChars, "byte count is 0");
    }


    @Test
    void TestTextFileCountNone() throws URISyntaxException {

        URL resource = CcwcTest.class.getResource("text.txt");
        File file = Paths.get(resource.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();

        String[] args = { absolutePath };
        
        var classUnderTest = new Ccwc();
        var cmd = new CommandLine(classUnderTest);
        cmd.execute(args);
        Result result = cmd.getExecutionResult();
        assertEquals(7137, result.countLines, "line count is 7137");
        assertEquals(58159, result.countWords, "word count is 58159");
        assertEquals(341836, result.countChars, "byte count is 341836");
    }

    @Test
    void TestTextFileCountAll() throws URISyntaxException {

        URL resource = CcwcTest.class.getResource("text.txt");
        File file = Paths.get(resource.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();

        String[] args = { "-l", "-w", "-c", absolutePath };
        
        var classUnderTest = new Ccwc();
        var cmd = new CommandLine(classUnderTest);
        cmd.execute(args);
        Result result = cmd.getExecutionResult();
        assertEquals(7137, result.countLines, "line count is 7137");
        assertEquals(58159, result.countWords, "word count is 58159");
        assertEquals(341836, result.countChars, "byte count is 341836");
    }
}