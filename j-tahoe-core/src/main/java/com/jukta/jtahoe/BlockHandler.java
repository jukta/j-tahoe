package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JElement;

/**
 * @since 1.0
 */
public interface BlockHandler {

    void startRendering(Attrs attrs);

    void before(String blockName, Attrs attrs, Block block);

    void after(String blockName,  Attrs attrs, JElement jElement, Block block);

    void stopRendering(Attrs attrs, JElement jElement);

}
