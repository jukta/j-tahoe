package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

/**
 * Created by Dmitriy Dobrovolskiy on 26.04.2016.
 *
 * @since *.*.*
 */
public class TagHandler extends AbstractHandler {
    public TagHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
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
