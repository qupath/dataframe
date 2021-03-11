import net.mahdilamb.dataframe.DataFrame;
import org.junit.Test;

import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;

public class Tests {
    static DataFrame loadFromResources(final String name) {
        return DataFrame.from(new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(name)).getFile()));
    }

    @Test
    public void CSVTest() {
        System.out.println(loadFromResources("iris.csv").query("species=='setosa'").query("petal_length > 1").tail());

    }


}
