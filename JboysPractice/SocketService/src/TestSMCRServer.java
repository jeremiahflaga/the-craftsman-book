import java.io.IOException;
import java.net.Socket;

public class TestSMCRServer implements SocketServer {
    public boolean fileReceived = false;

    @Override
    public void serve(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}
