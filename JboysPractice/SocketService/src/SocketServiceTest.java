import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    @Test
    public void testManyConnections() throws Exception {
        SocketService socketService = new SocketService();
        socketService.serve(999);
        for (int i = 0; i < 10; i++)
            connect(999);
        socketService.close();
        assertEquals(10, socketService.connections());
    }

    @Test
    public void testSendMessage() throws Exception {
        SocketService socketService = new SocketService();
        socketService.serve(999, new HelloServer());
        Socket socket = new Socket("localhost", 999);

        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String answer = bufferedReader.readLine();
        socket.close();

        assertEquals("Hello", answer);
    }

    private void connect(int port) {
        try {
            Socket s = new Socket("localhost", port);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {

            }
            s.close();
        } catch (IOException ex) {
            fail("Could not connect");
        }
    }
}