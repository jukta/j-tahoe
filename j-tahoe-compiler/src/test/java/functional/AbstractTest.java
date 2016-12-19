package functional;

import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.BlockFactory;
import com.jukta.jtahoe.RuntimeBlockFactory;
import com.jukta.jtahoe.gen.xml.XthBlockModelProvider;
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
            blockFactory = new RuntimeBlockFactory(new XthBlockModelProvider());
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
