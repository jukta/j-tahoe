package functional;

import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.BlockFactory;
import com.jukta.jtahoe.RuntimeBlockFactory;
import com.jukta.jtahoe.gen.xml.XmlBlockModelProvider;
import org.junit.Before;

/**
 * @since 1.0
 */
public class AbstractTest {
    private String blocksFolder = "blocks";
    protected static BlockFactory blockFactory;

    @Before
    public void setUp() {
        if (blockFactory == null) {
            blockFactory = new RuntimeBlockFactory(new XmlBlockModelProvider(blocksFolder));
        }
    }

    protected Block newBlockInstance(String blockName) {
        try {
            Thread.currentThread().setContextClassLoader(blockFactory.getClassLoader());
            return blockFactory.create(blockName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
