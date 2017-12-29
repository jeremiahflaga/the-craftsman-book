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


    private abstract class FileComparator {
        abstract void writeFirstFile(PrintWriter w);
        abstract void writeSecondFile(PrintWriter w);

        void compare(boolean expected) throws Exception {
            File f1 = new File("f1");
            File f2 = new File("f2");
            PrintWriter w1 = new PrintWriter(new FileWriter(f1));
            PrintWriter w2 = new PrintWriter(new FileWriter(f2));
            writeFirstFile(w1);
            writeSecondFile(w2);
            w1.close();
            w2.close();
            assertEquals(expected, filesAreTheSame(f1, f2), "(f1,f2)");
            assertEquals(expected, filesAreTheSame(f2, f1), "(f2,f1)");
            f1.delete();
            f2.delete();
        }
    }

    @Test
    public void testOneFileLongerThanTheOther() throws Exception {
        FileComparator c = new FileComparator() {
            void writeFirstFile(PrintWriter w) {
                w.println("hi there");
            }
            void writeSecondFile(PrintWriter w) {
                w.println("hi there you");
            }
        };
        c.compare(false);
    }

    @Test
    public void testFilesAreDifferentInTheMiddle() throws Exception {
        FileComparator c = new FileComparator() {
            void writeFirstFile(PrintWriter w) {
                w.println("hi there");
            }
            void writeSecondFile(PrintWriter w) {
                w.println("hi their");
            }
        };
        c.compare(false);
    }

    @Test
    public void testSecondLineDifferent() throws Exception {
        FileComparator c = new FileComparator() {
            void writeFirstFile(PrintWriter w) {
                w.println("hi there");
                w.println("This is fun");
            }
            void writeSecondFile(PrintWriter w) {
                w.println("hi there");
                w.println("This isn't fun");
            }
        };
        c.compare(false);
    }

    @Test
    public void testFilesSame() throws Exception {
        FileComparator c = new FileComparator() {
            void writeFirstFile(PrintWriter w) {
                w.println("hi there");
            }
            void writeSecondFile(PrintWriter w) {
                w.println("hi there");
            }
        };
        c.compare(true);
    }

    @Test
    public void testMultipleLinesSame() throws Exception {
        FileComparator c = new FileComparator() {
            void writeFirstFile(PrintWriter w) {
                w.println("hi there");
                w.println("this is fun");
                w.println("Lots of fun");
            }
            void writeSecondFile(PrintWriter w) {
                w.println("hi there");
                w.println("this is fun");
                w.println("Lots of fun");
            }
        };
        c.compare(true);
    }
}
