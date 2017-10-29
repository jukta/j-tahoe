package functional;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.BlockHandler;
import com.jukta.jtahoe.BlockHandlerAdapter;
import com.jukta.jtahoe.jschema.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
    public void cascadeDefInheritance() {
        Block b = newBlockInstance("test.CascadeDefInheritance_C");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody()
                .addElement(new JText("A1"))
                .addElement(new JBody()
                        .addElement(new JText("B1"))
                        .addElement(new JBody().addElement(new JText("C1")))
                        .addElement(new JText("B3")))
                .addElement(new JText("A3"));
        assertEquals(expected, el);
    }

    @Test
    public void advancedDefInheritance() {
        Block b = newBlockInstance("test.AdvancedDefInheritance_C");
        JElement el = b.body(new Attrs());
//        System.out.println(el.toJson());
        JBody expected = new JBody().
                addElement(new JText("B1"))
                .addElement(new JBody()
                        .addElement(new JText("A1"))
                        .addElement(new JBody().addElement(new JBody().addElement(new JText("C1"))))
                        .addElement(new JText("A3")))
                .addElement(new JText("B3"));
        assertEquals(expected, el);
    }

    @Test
    public void duplicateDefs() {
        Block b = newBlockInstance("test.DuplicateDefs_B");
        JElement el = b.body(new Attrs());
//        System.out.println(el.toJson());
        JBody expected = new JBody()
                .addElement(new JBody().addElement(new JBody().addElement(new JText("B1"))))
                .addElement(new JBody().addElement(new JBody().addElement(new JText("B2"))));
        assertEquals(expected.toHtml(), el.toHtml());
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
        assertEquals(expected.toHtml(), el.toHtml());
    }

    @Test
    public void lowerCase() {
        Block b = newBlockInstance("test.lowercaseB");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JBody().addElement(new JText("A")));
        assertEquals(expected.toString(), el.toString());
    }


    @Test
    public void escaping() {
        Block b = newBlockInstance("test.escaping");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JBody().addElement(new JText("A \" \" B")));
        assertEquals(expected.toString(), el.toString());
    }

    @Test
    public void escape() {

        Block b = newBlockInstance("test.cdata");
        JElement el = b.body(new Attrs().set("a", "A"));

        JBody expected = new JBody().addElement(new JText("" +
                "&lt;th:block name=\"escaping1\"&gt;" +
                "A \" \" B" +
                "&lt;/th:block&gt;"));
        assertEquals(expected.toString(), el.toString());
    }

    @Test
    public void blockHandler() {
        Block b = newBlockInstance("test.AdvancedComposition_C1");
        Attrs attrs = new Attrs();
        List<String> res1 = new ArrayList<>();
        List<String> res2 = new ArrayList<>();
        attrs.setBlockHandler(new BlockHandlerAdapter() {
            @Override
            public void before(String blockName, Attrs attrs, Block block) {
                res1.add(blockName);
            }

            @Override
            public void after(String blockName, Attrs a, JElement jElement, Block block) {
                res2.add(blockName + " " + jElement);

            }
        });
        b.body(attrs);

        List<String> before = new ArrayList<>();
        before.add("test.AdvancedComposition_C1");
        before.add("test.AdvancedComposition_A1");
        before.add("test.AdvancedComposition_B1");

        assertEquals(before, res1);

        List<String> after = new ArrayList<>();
        after.add("test.AdvancedComposition_B1 C");
        after.add("test.AdvancedComposition_A1 C");
        after.add("test.AdvancedComposition_C1 C");

        assertEquals(after, res2);
    }

    @Test
    public void recursion() {
        Block b = newBlockInstance("test.Recursion_B");
        Attrs attrs = new Attrs();

        JElement el = b.body(attrs);

        JBody expected = new JBody().addElement(new JBody()
                .addElement(new JText("A"))
                .addElement(new JBody()
                        .addElement(new JText("A1"))
                        .addElement(new JBody()
                                .addElement(new JText("A"))
                                .addElement(new JBody().addElement(new JText("A2")))
                        )
                )
        );

        assertEquals(expected, el);
    }

    @Test
    public void seftClosing() {
        Block b = newBlockInstance("test.SelfClosing");
        Attrs attrs = new Attrs();

        JElement el = b.body(attrs);
        assertEquals("<br/>", el.toHtml());
    }

    @Test
    public void comments() {
        Block b = newBlockInstance("test.Comments");
        Attrs attrs = new Attrs();
        JElement el = b.body(attrs);

        assertEquals("<!-- comments<span>a</span>-->A<!-- comments b -->", el.toHtml());
    }

    @Test
    public void cdata() {
        Block b = newBlockInstance("test.CDATA");
        Attrs attrs = new Attrs();
        JElement el = b.body(attrs);

        assertEquals("<![CDATA[some data]]>", el.toHtml());
    }

}
