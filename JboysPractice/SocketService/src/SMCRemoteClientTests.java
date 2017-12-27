import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class SMCRemoteClientTests {

    SMCRemoteClient client = new SMCRemoteClient();

    @Test
    public void testParseCommandLine() throws Exception {
        client.parseCommandLine(new String[]{"filename"});
        assertEquals("filename", client.filename());
    }

    @Test
    public void testParseInvalidCommandLine() {
        boolean result = client.parseCommandLine(new String[0]);
        assertTrue(!result, "result should be false");
    }

    @Test
    public void testFileDoesNotExist() throws Exception {
        client.setFilename("thisFileDoesNotExist");
        boolean prepared = client.prepareFile();
        assertEquals(false, prepared);
    }

    @Test
    public void testCountBytesInFile() throws Exception {
        File file = new File("testFile");
        FileOutputStream stream = new FileOutputStream(file);
        stream.write("some text".getBytes());
        stream.close();

        client.setFilename("testfile");
        boolean prepared = client.prepareFile();

        assertTrue(prepared);
        assertEquals(9, client.getFileLength());
    }
}