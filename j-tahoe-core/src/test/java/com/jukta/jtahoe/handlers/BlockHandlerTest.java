package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.model.NamedNode;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * @since 1.0
 */
public class BlockHandlerTest {

    @Test
    public void test1() {
        BlockHandler bh = new BlockHandler(null, new NamedNode(null, null, new HashMap<String, String>(), null), null);
        String ex = "${name}";
        String exp = bh.parseExp(ex, false);
        assertEquals("eval(attrs, \"attrs.name\")", exp);
    }

    @Test
    public void test2() {
        BlockHandler bh = new BlockHandler(null, new NamedNode(null, null, new HashMap<String, String>(), null), null);
        String ex = "${name}";
        String exp = bh.parseExp(ex, true);
        assertEquals("eval(attrs, \"attrs.name\")", exp);
    }

    @Test
    public void test3() {
        BlockHandler bh = new BlockHandler(null, new NamedNode(null, null, new HashMap<String, String>(), null), null);
        String ex = "${a ' b ' 'c '  ' d' 'e f g ' h.i1 }";
        String exp = bh.parseExp(ex, true);
        assertEquals("eval(attrs, \"attrs.a ' b ' 'c '  ' d' 'e f g ' attrs.h.i1 \")", exp);
    }

    @Test
    public void test4() {
        BlockHandler bh = new BlockHandler(null, new NamedNode(null, null, new HashMap<String, String>(), null), null);
        String ex = "${a} ${b} ${'c'}";
        String exp = bh.parseExp(ex, true);
        assertEquals("eval(attrs, \"attrs.a\") + \" \" + eval(attrs, \"attrs.b\") + \" \" + eval(attrs, \"'c'\")", exp);
    }

}
