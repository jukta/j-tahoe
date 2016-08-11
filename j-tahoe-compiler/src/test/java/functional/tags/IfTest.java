package functional.tags;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;
import com.jukta.jtahoe.jschema.JText;
import functional.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @since 1.0
 */
public class IfTest extends AbstractTest {

    @Test
    public void IfPositive() {
        Block b = newBlockInstance("test.tags.If_A");
        JElement el = b.body(new Attrs().set("str", "_"));
        JBody expected = new JBody()
                .addElement(new JBody()
                        .addElement(new JText("A")));
        assertEquals(expected, el);
    }

    @Test
    public void IfNegative() {
        Block b = newBlockInstance("test.tags.If_A");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody()
                .addElement(new JBody());
        assertEquals(expected, el);
    }

    @Test
    public void IfSetPositive() {
        Block b = newBlockInstance("test.tags.If_Set");
        JElement el = b.body(new Attrs().set("str", "_"));
        JBody expected = new JBody()
                .addElement(new JBody())
                .addElement(new JText("B"));
        assertEquals(expected, el);
    }

    @Test
    public void IfSetNegative() {
        Block b = newBlockInstance("test.tags.If_Set");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody()
                .addElement(new JBody())
                .addElement(new JText(""));
        assertEquals(expected, el);
    }

}
