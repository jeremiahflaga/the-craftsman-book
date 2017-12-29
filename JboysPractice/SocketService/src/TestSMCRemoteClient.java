import java.io.File;
import java.io.IOException;

public class TestSMCRemoteClient {
    public static void createTestFile(String filename, String content) throws IOException {
        File resultFile = new File(filename);
        resultFile.createNewFile();

    }
}
