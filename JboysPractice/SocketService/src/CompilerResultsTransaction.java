import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class CompilerResultsTransaction implements Serializable {
    private String filename;

    public CompilerResultsTransaction(String filename) {
        this.filename = filename;
    }

    public void write() throws Exception {
        File resultFile = new File(filename);
        resultFile.createNewFile();
    }
}
