import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

public class FileCarrier implements Serializable {
    private String fileName;
    private LinkedList<String> lines = new LinkedList();
    private char[] contents;

    public FileCarrier(String fileName) throws Exception {
        this.fileName = fileName;
        loadLines();
    }

    private void loadLines() throws IOException {
        BufferedReader reader = makeBufferedReader();
        String line;
        while ((line = reader.readLine()) != null)
            lines.add(line);
        reader.close();
    }

    private BufferedReader makeBufferedReader() throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
    }

    public void write() throws Exception {
        PrintStream printStream = makePrintStream();
        for (Iterator iterator = lines.iterator(); iterator.hasNext(); )
            printStream.println((String) iterator.next());
        printStream.close();
    }

    private PrintStream makePrintStream() throws FileNotFoundException {
        return new PrintStream(new FileOutputStream(fileName));
    }

    public String getFileName() {
        return fileName;
    }

    public char[] getContents() {
        //NOTE: this is temporary
        return lines.get(0).toCharArray();
    }
}