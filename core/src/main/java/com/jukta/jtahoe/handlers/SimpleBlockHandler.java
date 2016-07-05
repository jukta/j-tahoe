package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.GenContext;

import java.util.Map;

/**
 * Created by Dmitriy Dobrovolskiy on 06.07.2016.
 *
 * @since *.*.*
 */
public abstract class SimpleBlockHandler extends BlockHandler {
    public SimpleBlockHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
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
}
