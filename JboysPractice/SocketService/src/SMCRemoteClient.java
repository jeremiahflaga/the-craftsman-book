import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class SMCRemoteClient {
    private String itsFilename;
    private long itsFileLength;

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
        File file = new File(itsFilename);
        if (file.exists()) {
            itsFileLength = file.length();
            return true;
        } else {
            return false;
        }
    }

    public long getFileLength() {
        return itsFileLength;
    }

    public boolean connect() {
        try {
            Socket s = new Socket("localhost", 9000);
            return true;
        } catch (IOException e) {
        }
        return false;
    }
}
