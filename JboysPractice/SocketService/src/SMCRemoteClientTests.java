import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SMCRemoteClientTests {

    private static final int SMCPORT = 9000;
    SMCRemoteClient client;
    TestSMCRServer server;
    SocketService smc;

    @BeforeEach
    public void setup() throws Exception {
        client = new SMCRemoteClient();
        server = new TestSMCRServer();
        smc = new SocketService(SMCPORT, server);
    }

    @AfterEach
    public void tearDown() throws Exception {
        smc.close();
    }

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
        File file = createTestFile("testFile", "some text");
        client.setFilename("testfile");
        boolean prepared = client.prepareFile();
        file.delete();

        assertTrue(prepared);
        assertEquals(9, client.getFileLength());
    }

    @Test
    public void testConnectionToSMCRemoteServer() throws Exception {
        boolean connection = client.connect();
        assertTrue(connection);
    }

    @Test
    public void testCompileFile() throws Exception {
        File file = createTestFile("testSendFile", "I am sending this file.");
        client.setFilename("testSendFile");

        assertTrue(client.connect());
        assertTrue(client.prepareFile());
        assertTrue(client.compileFile());

        Thread.sleep(50);

        assertTrue(server.fileReceived);
        assertEquals("testSendFile", server.filename);
        assertEquals(23, server.fileLength);
        assertEquals("I am sending this file.", new String(server.content));

        file.delete();

        File resultFile = new File("resultFile.java");
        assertTrue(resultFile.exists(), "Result file does not exist");
        resultFile.delete();
    }

    private File createTestFile(String name, String content) throws IOException {
        File file = new File(name);
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(content.getBytes());
        stream.close();
        return file;
    }
}