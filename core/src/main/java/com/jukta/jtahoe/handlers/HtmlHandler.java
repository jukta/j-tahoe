package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.GenContext;

import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public class HtmlHandler extends AbstractHandler {

    public HtmlHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
    }

    @Override
    public void end() {
        String el = "new com.jukta.jtahoe.jschema.JTag(\"" + getName() + "\").setjBody(" + body + ")";
        getParent().addElement(el);
    }

    String body = "new com.jukta.jtahoe.jschema.JBody()";

    @Override
    public void addElement(String element) {
        body += ".addElement(" + element + ")";
    }

    @Override
    public void appendCode(String code) {
        super.appendCode(code);
    }
}
