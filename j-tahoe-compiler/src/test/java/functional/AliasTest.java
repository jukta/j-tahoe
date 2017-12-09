package functional;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.jschema.JElement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @since 1.0
 */
public class AliasTest extends AbstractTest {

    @Test
    public void blockAlias() {
        Block b = newBlockInstance("test.BlockAlias_D");
        Attrs attrs = new Attrs();
        JElement el = b.body(attrs);
        System.out.println(el.toHtml());
        assertEquals("ABB", el.toHtml());
    }

}
