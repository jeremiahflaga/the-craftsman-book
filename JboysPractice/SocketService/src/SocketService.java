import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService {
    private ServerSocket serverSocket = null;
    private int connections = 0;
    private Thread serverThread = null;

    public void serve(int port) throws  Exception {
        serverSocket = new ServerSocket(port);
        serverThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = serverSocket.accept();
                            socket.close();
                            connections++;
                        } catch (IOException ex) {

                        }
                    }
                }
        );
        serverThread.start();
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public int connections() {
        return connections;
    }
}
