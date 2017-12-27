import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SMCRemoteClientTests {
    @Test
    public void testParseCommandLine() throws Exception {
        SMCRemoteClient client = new SMCRemoteClient();
        client.parseCommandLine(new String[]{"filename"});
        assertEquals("filename", client.filename());
    }

    @Test
    public void testParseInvalidCommandLine() {
        SMCRemoteClient client = new SMCRemoteClient();
        boolean result = client.parseCommandLine(new String[0]);
        assertTrue(!result, "result should be false");
    }
}