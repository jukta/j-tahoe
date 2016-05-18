package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.GenContext;

import java.util.Map;

/**
 * Created by Dmitriy Dobrovolskiy on 26.04.2016.
 *
 * @since *.*.*
 */
public class TagHandler extends AbstractHandler {
    public TagHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
    }

    @Override
    public void end() {
        String name = getAttrs().get("name");
        String el = "new JTag(\"" + name + "\").setjBody(null)";
//        String cd = "JBody " + getVarName() + " = new JBody();\n";
//        appendCode(cd);
//        String el = "new JTag(\"" + name + "\").setjBody(" + getVarName() + ")";
        getParent().addElement(el);
    }

    String body = "";

    @Override
    public void addElement(String element) {
        body += getVarName() + ".addElement(" + element + ");\n";
    }

    @Override
    public void appendCode(String code) {
        super.appendCode(code);
    }
}
