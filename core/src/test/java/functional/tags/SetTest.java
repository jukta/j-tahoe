package functional.tags;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.jschema.*;
import functional.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @since 1.0
 */
public class SetTest extends AbstractTest {

    @Test
    public void SetLocal() {
        Block b = newBlockInstance("test.tags.SetLocal_A");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JText("A"));
        assertEquals(expected, el);
    }

    @Test
    public void SetLocalOverride() {
        Block b = newBlockInstance("test.tags.SetLocalOverride_A");
        JElement el = b.body(new Attrs()
                .set("a", "a")
                .set("b", "b"));
        JBody expected = new JBody()
                .addElement(new JText("a"))
                .addElement(new JText("B"));
        assertEquals(expected, el);
    }

    @Test
    public void SetGlobal() {
        Block b = newBlockInstance("test.tags.SetGlobal_A");
        Attrs attrs = new Attrs();
        b.body(attrs);
        assertEquals("A", attrs.getAttribute("a"));
    }

    @Test
    public void SetGlobalOverride() {
        Block b = newBlockInstance("test.tags.SetGlobalOverride_A");
        Attrs attrs = new Attrs()
                .setAttribute("a", "a")
                .setAttribute("b", "b");
        JElement el = b.body(attrs);
        b.body(attrs);
        JBody expected = new JBody()
                .addElement(new JText("a"))
                .addElement(new JText("B"));
        assertEquals("a", attrs.getAttribute("a"));
        assertEquals("B", attrs.getAttribute("b"));
        assertEquals(expected, el);
    }

    @Test
    public void SetBlockRef() {
        Block b = newBlockInstance("test.tags.SetBlockRef_B");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody()
                .addElement(new JBody()
                        .addElement(new JBody()
                                .addElement(new JText("B"))));
        assertEquals(expected, el);
    }


}
