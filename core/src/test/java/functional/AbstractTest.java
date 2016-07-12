package functional;

import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.file.JTahoeXml;
import com.jukta.jtahoe.gen.DirHandler;
import com.jukta.jtahoe.loader.MemoryClassLoader;
import com.jukta.jtahoe.resource.ResourceType;
import com.jukta.jtahoe.resource.Resources;
import org.junit.Before;

import java.io.File;
import java.util.List;

/**
 * @since 1.0
 */
public class AbstractTest {
    private String blocksFolder = "blocks";
    protected static ClassLoader classLoader;

    @Before
    public void setUp() {
        if (classLoader == null) {
            List<JTahoeXml> xmlFilesList = new Resources(blocksFolder).getFiles(ResourceType.XML);
            try {
                classLoader = new MemoryClassLoader(new DirHandler(new File("/")).getJavaFiles(xmlFilesList));
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
            return (Block) Class.forName(blockName, true, classLoader).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
