package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

/**
 * Created by Dmitriy Dobrovolskiy on 11.04.2016.
 *
 * @since *.*.*
 */
public class IfHandler extends AbstractHandler {
    public IfHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    @Override
    public void end() {
        String exp = parseExp(getAttrs().get("exp"), false);
        String cd = "JBody " + getVarName() + " = new JBody();\n";
        cd += "if ( ((boolean)" + exp + ") ) {\n";
        cd += body;
        cd += "}";
        getParent().appendCode(cd);
        getParent().addElement(getVarName());
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