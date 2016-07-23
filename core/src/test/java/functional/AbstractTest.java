package functional;

import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.BlockFactory;
import com.jukta.jtahoe.gen.xml.XmlBlockModelProvider;
import com.jukta.jtahoe.loader.MemoryClassLoader;
import com.jukta.jtahoe.model.NodeProcessor;
import org.junit.Before;

import javax.tools.JavaFileObject;
import java.util.List;

/**
 * @since 1.0
 */
public class AbstractTest {
    private String blocksFolder = "blocks";
    protected static ClassLoader classLoader;
    protected static BlockFactory blockFactory;

    @Before
    public void setUp() {
        if (classLoader == null) {
            try {
                List<JavaFileObject> javaFileObjects = new NodeProcessor().process(new XmlBlockModelProvider(blocksFolder));
                classLoader = new MemoryClassLoader(javaFileObjects);
                blockFactory = new BlockFactory(classLoader);
                Thread.currentThread().setContextClassLoader(classLoader);
            } catch (RuntimeException e) {
                throw e;
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    protected Block newBlockInstance(String blockName) {
        try {
            return blockFactory.create(blockName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
