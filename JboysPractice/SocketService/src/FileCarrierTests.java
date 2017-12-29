import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileCarrierTests {
    @Test
    public void testAFile() throws Exception {
        final String TESTFILE = "testFile.txt";
        final String TESTSTRING = "test";

        createFile(TESTFILE, TESTSTRING);

        FileCarrier fileCarrier = new FileCarrier(TESTFILE);
        fileCarrier.write();

        assertTrue(new File(TESTFILE).exists());

        String contents = readFile(TESTFILE);
        assertEquals(TESTSTRING, contents);
    }

    private String readFile(final String TESTFILE) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(TESTFILE));
        String line = reader.readLine();
        return line;
    }
    private void createFile(final String TESTFILE,
                            final String TESTSTRING) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(TESTFILE));
        writer.println(TESTSTRING);
        writer.close();
    }
}
