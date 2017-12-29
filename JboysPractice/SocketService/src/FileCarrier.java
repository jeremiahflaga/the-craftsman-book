import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;

public class FileCarrier implements Serializable {
    private String fileName;
    private char[] contents;

    public FileCarrier(String fileName) throws Exception {
        File f = new File(fileName);
        this.fileName = fileName;

        int fileSize = (int)f.length();
        contents = new char[fileSize];

        FileReader reader = new FileReader(f);
        reader.read(contents);
        reader.close();
    }

    public void write() throws Exception {
        FileWriter writer = new FileWriter(fileName);
        writer.write(contents);
        writer.close();
    }

    public String getFileName() {
        return fileName;
    }

    public char[] getContents() {
        return contents;
    }
}