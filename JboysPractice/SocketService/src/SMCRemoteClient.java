import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SMCRemoteClient {
    private String itsFilename;
    private long itsFileLength;

    private ObjectInputStream socketsInputStream;
    private ObjectOutputStream socketsOutputStream;
    private BufferedReader fileReader;

    private List<Socket> sockets = Collections.synchronizedList(new LinkedList<>());

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
            sockets.add(socket);

            socketsInputStream = new ObjectInputStream(socket.getInputStream());
            socketsOutputStream = new ObjectOutputStream(socket.getOutputStream());

            String headerLine = (String) socketsInputStream.readObject();
            connectionStatus = headerLine != null && headerLine.startsWith("SMCR");
        } catch (Exception e) {
            e.printStackTrace();
            connectionStatus = false;
        }
        return connectionStatus;
    }

    public boolean compileFile() {
        boolean fileCompiled = false;
        char buffer[] = new char[(int) itsFileLength];
        try {
            fileReader.read(buffer);
            CompileFileTransaction compileFileTransaction =
                    new CompileFileTransaction(itsFilename, buffer);
            socketsOutputStream.writeObject(compileFileTransaction);
            socketsOutputStream.flush();

            Object response = socketsInputStream.readObject();
            CompilerResultsTransaction compilerResultsTransaction = (CompilerResultsTransaction) response;
            compilerResultsTransaction.write();

            fileCompiled = true;
        } catch (Exception ex) {
            fileCompiled = false;
        }
        return fileCompiled;
    }

    public void close() throws Exception {
        for (Socket socket : sockets)
            socket.close();
    }
}
