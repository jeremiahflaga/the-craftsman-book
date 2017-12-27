public class SMCRemoteClient {
    private String itsFilename;

    public void parseCommandLine(String[] args) {
        itsFilename = args[0];
    }

    public String filename() {
        return itsFilename;
    }
}
