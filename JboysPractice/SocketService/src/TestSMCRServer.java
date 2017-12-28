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
            parse(inputStream.readObject());
        } catch (Exception e) {
        }
    }

    private void parse(Object cmd) throws Exception {
        if (cmd != null) {
            if (cmd instanceof CompileFileTransaction) {
                CompileFileTransaction compileFileTransaction = (CompileFileTransaction) cmd;
                filename = compileFileTransaction.getFilename();
                content = compileFileTransaction.getContents();
                fileLength = content.length;
                fileReceived = true;
            }
        }
    }
}
