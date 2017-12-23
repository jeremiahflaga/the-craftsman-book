import java.io.IOException;
import java.net.ServerSocket;

public class SocketService {
    private ServerSocket serverSocket = null;

    public void serve(int port) throws  Exception {
        serverSocket = new ServerSocket(port);
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public int connections() {
        return 0;
    }
}
