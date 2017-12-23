import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class SocketServiceTest {
    @Test
    public void testOneConnection() throws Exception {
        SocketService ss = new SocketService();
        ss.serve(999);
        connect(999);
        ss.close();
        assertEquals(1, ss.connections());
    }

    private void connect(int port) {
        try {
            Socket s = new Socket("localhost", port);
            s.close();
        } catch (IOException ex) {
            fail("Could not connect");
        }
    }
}