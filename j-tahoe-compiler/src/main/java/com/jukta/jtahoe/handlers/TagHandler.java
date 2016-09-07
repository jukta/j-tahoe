package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

/**
 * Created by Dmitriy Dobrovolskiy on 26.04.2016.
 *
 * @since *.*.*
 */
public class TagHandler extends AbstractHandler {

    private String body = "";

    public TagHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    @Override
    public void start() {
        super.start();
        String cd = "JAttrs attrs" + getVarName() + " = new JAttrs();\n";
        appendCode(cd);
    }

    @Override
    public void end() {
        String cd = "JBody " + getVarName() + " = new JBody();\n";
        cd += body;
        appendCode(cd);

        String name = parseExp(getAttrs().get("name"), true);
        String el = "new JTag((String)" + name + ")";
        el += ".setAttrs(attrs" + getVarName() + ")";
        el += ".setjBody(" + getVarName() + ")";
        getParent().addElement(el);
    }


    @Override
    public void addElement(String element) {
        body += getVarName() + ".addElement(" + element + ");\n";
    }
}
