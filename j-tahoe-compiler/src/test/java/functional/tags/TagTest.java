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
public class TagTest extends AbstractTest {

    @Test
    public void simpleTag() {
        Block b = newBlockInstance("test.tags.SimpleTag_A");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JTag("sometag").setjBody(new JBody()));
        assertEquals(expected, el);
    }

    @Test
    public void attrTag() {
        Block b = newBlockInstance("test.tags.AttrTag_A");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JTag("sometag")
                .setAttrs(new JAttrs().addAttr("attr1", "val1").addAttr("attr2", null))
                .setjBody(new JBody()));

        assertEquals(expected, el);
    }

    @Test
    public void tagWithChild() {
        Block b = newBlockInstance("test.tags.TagWithChild_A");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JTag("sometag")
                .setAttrs(new JAttrs().addAttr("attr1", "val1").addAttr("attr2", null))
                .setjBody(new JBody().addElement(new JTag("div").setjBody(new JBody().addElement(new JText("hello"))))));

        assertEquals(expected, el);
    }
}
