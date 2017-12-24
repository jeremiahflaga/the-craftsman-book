import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class SocketServiceTest {
    private SocketService socketService = new SocketService();
    private int connections = 0;
    private SocketServer connectionsCounter;

    @BeforeEach
    public void setup() {
        connectionsCounter = new SocketServer() {
            @Override
            public void serve(Socket socket) {
                connections++;
            }
        };
    }

    @AfterEach
    public void cleanup() throws IOException {
        socketService.close();
    }

    @Test
    public void testOneConnection() throws Exception {
        socketService.serve(999, connectionsCounter);
        connect(999);
        assertEquals(1, socketService.connections());
    }

    @Test
    public void testManyConnections() throws Exception {
        socketService.serve(999, connectionsCounter);
        for (int i = 0; i < 10; i++)
            connect(999);
        assertEquals(10, socketService.connections());
    }

//    @Test
//    public void testSendMessage() throws Exception {
//        socketService.serve(999, new HelloServer());
//        Socket socket = new Socket("localhost", 999);
//
//        InputStream inputStream = socket.getInputStream();
//        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//        String answer = bufferedReader.readLine();
//        socket.close();
//
//        assertEquals("Hello", answer);
//    }

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