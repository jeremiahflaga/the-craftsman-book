import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileCarrierTests {

    @Test
    public void testAFile() throws Exception {
        final String ORIGINAL_FILENAME = "testFileCarrier.txt";
        final String RENAMED_FILENAME = "testFileCarrierRenamed.txt";
        File originalFile = new File(ORIGINAL_FILENAME);
        File renamedOriginal = new File(RENAMED_FILENAME);

        ensureFileIsRemoved(originalFile);
        ensureFileIsRemoved(renamedOriginal);

        createTestFile(originalFile);
        FileCarrier fc = new FileCarrier(ORIGINAL_FILENAME);
        rename(originalFile, renamedOriginal);
        fc.write();

        assertTrue(originalFile.exists());
        assertTrue(filesAreTheSame(originalFile, renamedOriginal));

        originalFile.delete();
        renamedOriginal.delete();



//        final String TESTFILE = "testFile.txt";
//        final String TESTSTRING = "test";
//
//        createFile(TESTFILE, TESTSTRING);
//
//        FileCarrier fileCarrier = new FileCarrier(TESTFILE);
//        new File(TESTFILE).delete();
//        fileCarrier.write();
//
//        assertTrue(new File(TESTFILE).exists());
//
//        String contents = readFile(TESTFILE);
//        assertEquals(TESTSTRING, contents);
    }

    private boolean filesAreTheSame(File file1, File file2) throws Exception {
        FileInputStream stream1 = new FileInputStream(file1);
        FileInputStream stream2 = new FileInputStream(file2);

        try {
            int c;
            while ((c = stream1.read()) != -1) {
                if (stream2.read() != c) {
                    return false;
                }
            }
            if (stream2.read() != -1)
                return false;
            else
                return true;
        } finally {
            stream1.close();
            stream2.close();
        }
    }

    private void rename(File oldFile, File newFile) {
        oldFile.renameTo(newFile);
        assertTrue(oldFile.exists() == false);
        assertTrue(newFile.exists());
    }

    private void createTestFile(File file) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("line one");
        writer.println("line two");
        writer.println("line three");
        writer.close();
    }

    private void ensureFileIsRemoved(File file) {
        if (file.exists()) file.delete();
        assertTrue(file.exists() == false);
    }

//    private String readFile(final String TESTFILE) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(TESTFILE));
//        String line = reader.readLine();
//        return line;
//    }
//
//    private void createFile(final String TESTFILE,
//                            final String TESTSTRING) throws IOException {
//        PrintWriter writer = new PrintWriter(new FileWriter(TESTFILE));
//        writer.println(TESTSTRING);
//        writer.close();
//    }
}
