import java.io.Serializable;

public class CompileFileTransaction implements Serializable {
    private final String filename;
    private final char[] buffer;

    public CompileFileTransaction(String filename, char[] buffer) {
        this.filename = filename;
        this.buffer = buffer;
    }

    public String getFilename() {
        return filename;
    }

    public char[] getContents() {
        return buffer;
    }
}
