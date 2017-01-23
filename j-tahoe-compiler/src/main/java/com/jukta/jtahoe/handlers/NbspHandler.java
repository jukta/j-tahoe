package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;
import com.jukta.jtahoe.jschema.JText;

import java.util.Map;

public class NbspHandler extends AbstractHandler {

    public NbspHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    @Override
    public void end() {
        String el = "new JText(\"&nbsp;\")";
        getParent().addElement(el);
    }

}
