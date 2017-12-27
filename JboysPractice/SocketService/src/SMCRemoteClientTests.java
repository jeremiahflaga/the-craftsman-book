import org.junit.jupiter.api.Test;

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
}