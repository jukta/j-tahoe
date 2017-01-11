package functional;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.jschema.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * @since 1.0
 */
public class GeneralTest extends AbstractTest {

    @Test
    public void simpleBlock() {
        Block b = newBlockInstance("test.SimpleBlock");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JText("A"));
        assertEquals(expected, el);
    }

    @Test
    public void simpleHtmlBlock() {
        Block b = newBlockInstance("test.SimpleHtmlBlock");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JTag("div").setAttrs(new JAttrs().addAttr("class", "a")).setjBody(new JBody().addElement(new JTag("span").setAttrs(new JAttrs().addAttr("id", "b")).setjBody(new JBody().addElement(new JText("A"))))));
        assertEquals(expected, el);
    }

    @Test
    public void inheritance() {
        Block b = newBlockInstance("test.Inheritance_B");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JText("A"));
        assertEquals(expected, el);
    }

    @Test
    public void defBlock() {
        Block b = newBlockInstance("test.DefBlock");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JText("A1")).addElement(new JBody().addElement(new JText("A2"))).addElement(new JText("A3"));
        assertEquals(expected, el);
    }

    @Test
    public void defInheritance() {
        Block b = newBlockInstance("test.DefInheritance_B");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JText("A1")).addElement(new JBody().addElement(new JText("B2"))).addElement(new JText("A3"));
        assertEquals(expected, el);
    }

    @Test
    public void namedDefInheritance() {
        Block b = newBlockInstance("test.NamedDefInheritance_B");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JText("A1")).addElement(new JBody().addElement(new JText("B2"))).addElement(new JText("A3")).addElement(new JBody().addElement(new JText("B4"))).addElement(new JText("A5"));
        assertEquals(expected, el);
    }

    @Test
    public void inheritanceLevel() {
        Block b = newBlockInstance("test.InheritanceLevel_C");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JText("A1"))
                .addElement(new JBody()
                        .addElement(new JText("B2_1"))
                        .addElement(new JBody().addElement(new JText("C2")))
                        .addElement(new JText("B2_2")))
                .addElement(new JText("A3"));
        assertEquals(expected, el);
    }

    @Test
    public void inheritanceComposition() {
        Block b = newBlockInstance("test.InheritanceComposition_B2");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JBody().addElement(new JBody()
                .addElement(new JText("B1")))
                .addElement(new JBody()
                        .addElement(new JText("A_1"))
                        .addElement(new JBody()
                                .addElement(new JBody()
                                        .addElement(new JText("A2_2")))
                                .addElement(new JText("B2")))
                        .addElement(new JText("A1_3"))));

        assertEquals(expected, el);
    }

    @Test
    public void advancedComposition() {
        Block b = newBlockInstance("test.AdvancedComposition_C1");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JBody().addElement(new JBody().addElement(new JBody().addElement(new JBody().addElement(new JText("C"))))));

        assertEquals(expected, el);
    }

    @Test
    public void blockConstructionArgs() {
        Block b = newBlockInstance("test.BlockConstructionArgs_C");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody()
                .addElement(new JTag("div").setjBody(new JBody()
                        .addElement(new JText("A"))))
                .addElement(new JText("B"));
        assertEquals(expected, el);
    }


}
