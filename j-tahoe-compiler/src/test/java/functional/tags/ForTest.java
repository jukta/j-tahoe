package functional.tags;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;
import com.jukta.jtahoe.jschema.JText;
import functional.AbstractTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * @since 1.0
 */
public class ForTest extends AbstractTest {

    @Test
    public void ForA() {
        Block b = newBlockInstance("test.tags.For_A");
        JElement el = b.body(new Attrs().set("a", Arrays.asList("A", "B")));
        JBody expected = new JBody()
                .addElement(new JBody()
                        .addElement(new JText("A"))
                        .addElement(new JText("B")));
        assertEquals(expected, el);
    }

    @Test
    public void ForEmptyA() {
        Block b = newBlockInstance("test.tags.For_A");
        JElement el = b.body(new Attrs().set("a", Collections.emptyList()));
        JBody expected = new JBody()
                .addElement(new JBody());
        assertEquals(expected, el);
    }
}
