import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author Sergey Sidorov
 */
public class Test {

    public static void main(String[] args) throws URISyntaxException, IOException {

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] r = resolver.getResources("classpath*:**/*.xml");
        for (Resource r1 : r) {
            System.out.println(r1.getURL());
        }

    }

}
