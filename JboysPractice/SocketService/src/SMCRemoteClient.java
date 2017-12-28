import java.io.*;
import java.net.Socket;

public class SMCRemoteClient {
    private String itsFilename;
    private long itsFileLength;

    private BufferedReader socketsInputStream;
    private ObjectOutputStream socketsOutputStream;
    private BufferedReader fileReader;

    public boolean parseCommandLine(String[] args) {
        try {
            itsFilename = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    public String filename() {
        return itsFilename;
    }

    public void setFilename(String filename) {
        itsFilename = filename;
    }

    public boolean prepareFile() {
        boolean filePrepared = false;
        File file = new File(itsFilename);
        if (file.exists()) {
            try {
                itsFileLength = file.length();
                fileReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file)));
                filePrepared = true;
            } catch (FileNotFoundException ex) {
                filePrepared = false;
                ex.printStackTrace();
            }
        }

        return filePrepared;
    }

    public long getFileLength() {
        return itsFileLength;
    }

    public boolean connect() {
        boolean connectionStatus;
        try {
            Socket socket = new Socket("localhost", 9000);
            socketsInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketsOutputStream = new ObjectOutputStream(socket.getOutputStream());

            String headerLine = socketsInputStream.readLine();
            connectionStatus = headerLine != null && headerLine.startsWith("SMCR");
        } catch (IOException e) {
            e.printStackTrace();
            connectionStatus = false;
        }
        return connectionStatus;
    }

    public boolean sendFile() {
        boolean fileSent = false;
        try {
            writeSendFileCommand();
            fileSent = true;
        } catch (Exception ex) {
            fileSent = false;
        }
        return fileSent;
    }

    private void writeSendFileCommand() throws IOException {
        socketsOutputStream.writeObject("Sending");
        socketsOutputStream.writeObject(itsFilename);
        socketsOutputStream.writeLong(itsFileLength);
        char buffer[] = new char[(int) itsFileLength];
        fileReader.read(buffer);
        socketsOutputStream.writeObject(buffer);
        socketsOutputStream.flush();
    }
}
