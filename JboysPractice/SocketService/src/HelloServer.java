import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

class HelloServer implements SocketServer {
    @Override
    public void serve(Socket socket) {
        try {
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println("Hello");
        } catch (IOException e) {
        }
    }
}
