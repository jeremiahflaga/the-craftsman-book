import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService {
    private ServerSocket serverSocket = null;
    private int connections = 0;
    private Thread serverThread = null;
    private boolean running = false;
    private SocketServer itsServer;

    public void serve(int port, SocketServer server) throws  Exception {
        itsServer = server;
        serverSocket = new ServerSocket(port);
        serverThread = makeServerThread();
        serverThread.start();
    }

    private Thread makeServerThread() {
        return new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        running = true;
                        while (running) {
                            acceptAndServeConnection();
                        }
                    }
                }
        );
    }

    private void acceptAndServeConnection() {
        try {
            Socket socket = serverSocket.accept();
            itsServer.serve(socket);
            socket.close();
            connections++;
        } catch (IOException ex) {
        }
    }

    public void close() throws IOException {
        running = false;
        serverSocket.close();
    }

    public int connections() {
        return connections;
    }
}
