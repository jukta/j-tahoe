package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JElement;

/**
 * @since 1.0
 */
public interface BlockHandler {

    void before(String block, Attrs attrs);

    void after(String block,  Attrs attrs, JElement jElement);

}
