package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public class HtmlHandler extends AbstractHandler {

    public HtmlHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    @Override
    public void end() {
        String cd = "JBody " + getVarName() + " = new JBody();\n";
        cd += body;
        appendCode(cd);
        String el = "new JTag(\"" + getName() + "\")";
        if (getAttrs().size() > 0) {
            el += ".setAttrs(new JAttrs()";
            for (Map.Entry<String, String> entry : getAttrs().entrySet()) {
                String val = entry.getValue();
                if (val != null) {
                    val = val.replaceAll("\n", "").replaceAll("\r", "").trim();
                }
                el += ".addAttr(\"" + entry.getKey() + "\", (String) " + parseExp(val, true) + ")";
            }
            el += ")";
        }
        el += ".setjBody(" + getVarName() + ")";
        getParent().addElement(el);
    }

    String body = "";

    @Override
    public void addElement(String element) {
        body += getVarName()+".addElement(" + element + ");\n";
    }

}
