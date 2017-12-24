import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

class EchoServer implements SocketServer {
    @Override
    public void serve(Socket socket) {
        try {
            BufferedReader bufferedReader = SocketService.getBufferedReader(socket);
            PrintStream printStream = SocketService.getPrintStream(socket);

            String token = bufferedReader.readLine();
            printStream.println(token);

        } catch (IOException e) {
        }
    }
}
