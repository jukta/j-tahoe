package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

/**
 * Created by Dmitriy Dobrovolskiy on 06.07.2016.
 *
 * @since *.*.*
 */
public abstract class SimpleBlockHandler extends BlockHandler {
    public SimpleBlockHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    @Override
    public void appendCode(String code) {
        throw new UnsupportedOperationException("Block " + getName() + " doesn't support body");
    }

    @Override
    public void addElement(String element) {
        throw new UnsupportedOperationException("Block " + getName() + " doesn't support body");
    }

    @Override
    public void addDef(String name, String body) {
        throw new UnsupportedOperationException("Block " + getName() + " doesn't support body");
    }

    public void text(String text) {
        if (text.trim().equals("")) return;
        throw new UnsupportedOperationException("Block " + getName() + " doesn't support body");
    }
}
