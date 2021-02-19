import net.mahdilamb.charts.dataframe.DataFrame;
import org.junit.Test;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Tests {
    static final DataFrame iris = DataFrame.from(new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("iris.csv")).getFile()));

    @Test
    public void CSVTest() {
        System.out.println(iris);
    }

    @Test
    public void clipboardTest() throws IOException, UnsupportedFlavorException {
        System.out.println(DataFrame.clipboardImport('\t', '"', StandardCharsets.UTF_8));

    }


}
