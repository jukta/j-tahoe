package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.GenContext;

import java.util.Map;

/**
 * Created by Dmitriy Dobrovolskiy on 11.04.2016.
 *
 * @since *.*.*
 */
public class IfHandler extends AbstractHandler {
    public IfHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
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