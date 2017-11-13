package functional;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.BlockHandlerAdapter;
import com.jukta.jtahoe.jschema.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @since 1.0
 */
public class OverrideTest extends AbstractTest {

    @Test
    public void blockOverride() {
        Block b = newBlockInstance("test.BlockOverride_D");
        Attrs attrs = new Attrs();
        JElement el = b.body(attrs);
        System.out.println(el.toHtml());
        assertEquals("AB", el.toHtml());
    }

    @Test
    public void overrideTemplate() {
        Block b = newBlockInstance("test.OverrideTemplate_A1");
        Attrs attrs = new Attrs();
        JElement el = b.body(attrs);
        System.out.println(el.toHtml());
        assertEquals("bcd", el.toHtml());
    }
}
