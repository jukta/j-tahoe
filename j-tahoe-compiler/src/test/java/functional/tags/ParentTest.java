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
public class ParentTest extends AbstractTest {

    @Test
    public void parent() {
        Block b = newBlockInstance("test.tags.Parent_B");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JBody().addElement(new JBody().addElement(new JText("A"))));
        assertEquals(expected, el);
    }

    @Test
    public void parentDefaultDef() {
        Block b = newBlockInstance("test.tags.ParentDefaultDef_B");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JBody().addElement(new JBody().addElement(new JText("A"))));
        assertEquals(expected, el);
    }

    @Test
    public void parentNamed() {
        Block b = newBlockInstance("test.tags.ParentNamed_B");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JBody().addElement(new JBody().addElement(new JText("A"))));
        assertEquals(expected, el);
    }

    @Test
    public void parentHierarchy() {
        Block b = newBlockInstance("test.tags.ParentHierarchy_C");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JBody().
                addElement(new JBody()
                        .addElement(new JBody()
                                .addElement(new JText("A")))
                        .addElement(new JText("B")))
                .addElement(new JText("C")));
        assertEquals(expected, el);
    }
}
