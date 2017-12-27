import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SMCRemoteClientTests {
    @Test
    public void testParseCommandLine() throws Exception {
        SMCRemoteClient client = new SMCRemoteClient();
        client.parseCommandLine(new String[]{"filename"});
        assertEquals("filename", client.filename());
    }
}