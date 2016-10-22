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
public class IncludeTest extends AbstractTest {

    @Test
    public void simpleTag() {
        Block b = newBlockInstance("test.include.SomeBlock_A");
        JElement el = b.body(new Attrs());
        JBody expected = new JBody().addElement(new JText("A"));
        assertEquals(expected, el);
    }

}
