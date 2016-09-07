package functional.tags;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.jschema.*;
import functional.AbstractTest;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @since 1.0
 */
public class TagTest extends AbstractTest {

    @Test
    public void simpleTag() {
        Block b = newBlockInstance("test.tags.SimpleTag_A");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JTag("sometag").setAttrs(new JAttrs()).setjBody(new JBody()));
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

    @Test
    public void attrTagWithIf() {
        Block b = newBlockInstance("test.tags.AttrTag_WithIf");
        JElement el = b.body(new Attrs().set("str", "_"));
        JBody expected = new JBody().addElement(new JTag("sometag")
                .setAttrs(new JAttrs().addAttr("attr1", "val1").addAttr("attr2", null))
                .setjBody(new JBody().addElement(new JBody())));

        assertEquals(expected, el);
    }

    @Test
    public void attrTagWithIfNeg() {
        Block b = newBlockInstance("test.tags.AttrTag_WithIf");
        JElement el = b.body(new Attrs().set("str", null));
        JBody expected = new JBody().addElement(new JTag("sometag")
                .setAttrs(new JAttrs().addAttr("attr2", null))
                .setjBody(new JBody().addElement(new JBody())));

        assertEquals(expected, el);
    }


    @Test
    public void attrTagWithFor() {
        Block b = newBlockInstance("test.tags.AttrTag_WithFor");
        JElement el = b.body(new Attrs().set("tag_name", "tagName").set("a", Arrays.asList("A", "B")));
        JBody expected = new JBody().addElement(new JTag("tagName")
                .setAttrs(new JAttrs().addAttr("A", "A").addAttr("B", "B").addAttr("attr2", null))
                .setjBody(new JBody().addElement(new JBody())));
        assertEquals(expected, el);
    }
}
