import java.io.*;
import java.net.Socket;

public class TestSMCRServer implements SocketServer {
    public boolean fileReceived = false;
    public String filename = "noFileName";
    public long fileLength = -1;
    public char[] content;
    public String command;

    private PrintStream outputStream;
    private ObjectInputStream inputStream;

    @Override
    public void serve(Socket socket) {
        try {
            outputStream = new PrintStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            //inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream.println("SMCR Test Server");
            outputStream.flush();
            String cmd = (String) inputStream.readObject();
            parse(cmd);
            //socket.close();
        } catch (Exception e) {
        }
    }

    private void parse(String cmd) throws Exception {
        if (cmd != null) {
            if (cmd.equals("Sending")) {
                filename = (String) inputStream.readObject();
                fileLength = inputStream.readLong();
                content = (char[]) inputStream.readObject();
                fileReceived = true;
            }
        }
    }
}
