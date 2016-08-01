package functional;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.DataHandlerProvider;
import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;
import com.jukta.jtahoe.jschema.JText;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @since 1.0
 */
public class DataHandlerTest extends AbstractTest {

    @Test
    public void dataHandler() {
        Block b = newBlockInstance("test.DataHandler_A");
        Attrs attrs = new Attrs();
        attrs.set("a", "A");
        attrs.setDataHandlerProvider(new DataHandlerProvider() {
            @Override
            public Attrs getData(String dataHandler, Attrs attrs) {
                assertEquals(attrs.get("a"), "A");
                assertEquals(dataHandler, "D");
                return attrs.set("b", "B");
            }
        });
        JElement el = b.body(attrs);
        JBody expected = new JBody().addElement(new JText("B"));
        assertEquals(expected, el);
    }

    @Test
    public void inheritanceDataHandler() {
        Block b = newBlockInstance("test.InheritanceDataHandler_B");
        Attrs attrs = new Attrs();
        attrs.set("a", "A");
        attrs.setDataHandlerProvider(new DataHandlerProvider() {
            @Override
            public Attrs getData(String dataHandler, Attrs attrs) {
                assertEquals(dataHandler, "D");
                attrs.set("b", "B");
                attrs.set("c", "C");
                attrs.set("d", "D");
                return attrs;
            }
        });
        JElement el = b.body(attrs);
        JBody expected = new JBody()
                .addElement(new JText("B"))
                .addElement(new JBody()
                        .addElement(new JBody().addElement(new JText("C")))
                        .addElement(new JText("D")));
        assertEquals(expected, el);
    }

    @Test
    public void compositionDataHandler() {
        Block b = newBlockInstance("test.CompositionDataHandler_B");
        Attrs attrs = new Attrs();
        attrs.set("a", "A");
        attrs.setDataHandlerProvider(new DataHandlerProvider() {
            @Override
            public Attrs getData(String dataHandler, Attrs attrs) {
                assertEquals(dataHandler, "D");
                return attrs.set("b", "B");
            }
        });
        JElement el = b.body(attrs);
        JBody expected = new JBody().addElement(new JBody().addElement(new JText("B")));
        assertEquals(expected, el);
    }

    @Test
    public void dataHandlerOverride() {
        Block b = newBlockInstance("test.DataHandlerOverride_B");
        Attrs attrs = new Attrs();
        attrs.set("a", "A");
        attrs.setDataHandlerProvider(new DataHandlerProvider() {
            @Override
            public Attrs getData(String dataHandler, Attrs attrs) {
                assertEquals(attrs.get("a"), "A");
                assertEquals(dataHandler, "D1");
                return attrs.set("b", "B");
            }
        });
        JElement el = b.body(attrs);
        JBody expected = new JBody().addElement(new JText("B"));
        assertEquals(expected, el);
    }

}
