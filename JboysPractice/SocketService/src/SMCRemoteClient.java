import java.io.File;

public class SMCRemoteClient {
    private String itsFilename;

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
            return true;
        } else {
            return false;
        }
    }
}
