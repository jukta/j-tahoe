package functional;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.jschema.JElement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @since 1.0
 */
public class MorpherTest extends AbstractTest {

    @Test
    public void test1() {
        Block b = newBlockInstance("test.MorpherBlockTest");
        Attrs attrs = new Attrs();
        JElement el = b.body(attrs);
        System.out.println(el.toHtml());
        assertEquals("AB", el.toHtml());
    }

}
