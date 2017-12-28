import com.sun.xml.internal.ws.message.stream.StreamHeader12;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
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
    public void cleanup() throws Exception {
        socketService.close();
    }

    @Test
    public void testOneConnection() throws Exception {
        socketService.serve(999, connectionsCounter);
        connect(999);
        assertEquals(1, connections);
    }

    @Test
    public void testManyConnections() throws Exception {
        socketService.serve(999, connectionsCounter);
        for (int i = 0; i < 10; i++)
            connect(999);
        assertEquals(10, connections);
    }

    @Test
    public void testSendMessage() throws Exception {
        socketService.serve(999, new HelloServer());
        Socket socket = new Socket("localhost", 999);

        BufferedReader bufferedReader = SocketService.getBufferedReader(socket);

        String answer = bufferedReader.readLine();
        socket.close();

        assertEquals("Hello", answer);
    }

    @Test
    public void testReceiveMessage() throws Exception {
        socketService.serve(999, new EchoServer());
        Socket socket = new Socket("localhost", 999);

        BufferedReader bufferedReader = SocketService.getBufferedReader(socket);
        PrintStream printStream = SocketService.getPrintStream(socket);

        printStream.println("MyMessage");
        String answer = bufferedReader.readLine();

        assertEquals("MyMessage", answer);

        socket.close();
    }

    @Test
    public void testMultiThreaded() throws Exception {
        socketService.serve(999, new EchoServer());

        Socket clientSocket1 = new Socket("localhost", 999);
        BufferedReader bufferedReader1 = SocketService.getBufferedReader(clientSocket1);
        PrintStream printStream1 = SocketService.getPrintStream(clientSocket1);

        Socket clientSocket2 = new Socket("localhost", 999);
        BufferedReader bufferedReader2 = SocketService.getBufferedReader(clientSocket2);
        PrintStream printStream2 = SocketService.getPrintStream(clientSocket2);

        printStream2.println("MyMessage 2");
        String answer2 = bufferedReader2.readLine();
        clientSocket2.close();

        printStream1.println("MyMessage 1");
        String answer1 = bufferedReader1.readLine();
        clientSocket1.close();

        assertEquals("MyMessage 2", answer2);
        assertEquals("MyMessage 1", answer1);
    }

    @Test
    public void testAllServerClosed() throws Exception {
        socketService.serve(999, new WaitThenClose());

        Socket clientSocket = new Socket("localhost", 999);
        Thread.sleep(20);

        assertEquals(1, WaitThenClose.threadsActive);
        socketService.close();
        assertEquals(0, WaitThenClose.threadsActive);
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