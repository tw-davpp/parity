package lucene;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class IndexTest {
    @Test
    public void test_index() throws IOException, SQLException {
        Index index = new Index(new File("./index"));
    }
}
