package com.jukta.jtahoe;

import com.jukta.jtahoe.gen.model.NamedNode;

import java.util.Iterator;

/**
 * @since 1.0
 */
public interface BlockModelProvider extends Iterable<NamedNode> {

    Iterator<NamedNode> iterator();

}
