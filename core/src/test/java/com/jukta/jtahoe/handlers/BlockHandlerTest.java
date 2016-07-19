package com.jukta.jtahoe.handlers;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * @since 1.0
 */
public class BlockHandlerTest {

    @Test
    public void test1() {
        BlockHandler bh = new BlockHandler(null, null, new HashMap<String, String>(), null);
        String ex = "${name}";
        String exp = bh.parseExp(ex, false);
        assertEquals("eval(attrs, \"attrs.name\")", exp);
    }

    @Test
    public void test2() {
        BlockHandler bh = new BlockHandler(null, null, new HashMap<String, String>(), null);
        String ex = "${name}";
        String exp = bh.parseExp(ex, true);
        assertEquals("eval(attrs, \"attrs.name\")", exp);
    }

    @Test
    public void test3() {
        BlockHandler bh = new BlockHandler(null, null, new HashMap<String, String>(), null);
        String ex = "${name ' name ' 'name '  ' name' }";
        String exp = bh.parseExp(ex, true);
        assertEquals("eval(attrs, \"attrs.name ' name ' 'name '  ' name' \")", exp);
    }

}
