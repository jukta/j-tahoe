package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JElement;

/**
 * @since 1.0
 */
public class BlockHandlerAdapter implements BlockHandler {

    @Override
    public void before(String blockName, Attrs attrs, Block block) {}

    @Override
    public void after(String blockName, Attrs attrs, JElement jElement, Block block) {}
}
