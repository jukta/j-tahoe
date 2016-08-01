import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Sergey Sidorov
 */
public class Test {

    public static void main(String[] args) throws URISyntaxException, IOException {

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource[] r = resolver.getResources("classpath*:/**/MANIFEST.MF");
        for (Resource r1 : r) {
            System.out.println(r1.getURL());
        }

    }

}
