import java.io.*;
import java.net.Socket;

public class TestSMCRServer implements SocketServer {
    public boolean fileReceived = false;
    public String filename = "noFileName";
    public long fileLength = -1;
    public char[] content;
    public String command;

    private PrintStream outputStream;
    private BufferedReader inputStream;

    @Override
    public void serve(Socket socket) {
        try {
            outputStream = new PrintStream(socket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream.println("SMCR Test Server");
            outputStream.flush();
            String cmd = inputStream.readLine();
            parse(cmd);
            //socket.close();
        } catch (Exception e) {
        }
    }

    private void parse(String cmd) throws IOException {
        if (cmd != null) {
            if (cmd.equals("Sending")) {
                filename = inputStream.readLine();
                fileLength = Long.parseLong(inputStream.readLine());
                content = new char[(int)fileLength];
                inputStream.read(content, 0, (int)fileLength);
                fileReceived = true;
            }
        }
    }
}
