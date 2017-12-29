import java.io.Serializable;

public class CompilerResultsTransaction implements Serializable {
    private FileCarrier resultFile;

    public CompilerResultsTransaction(String filename) throws Exception {
        resultFile = new FileCarrier(filename);
    }

    public void write() throws Exception {
        resultFile.write();
    }
}
