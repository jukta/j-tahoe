package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitriy Dobrovolskiy on 26.04.2016.
 *
 * @since *.*.*
 */
public class TagHandler extends AbstractHandler {

    private Map<String, String> attrs = new HashMap<>();
    private String body = "";

    public TagHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    @Override
    public void end() {

        String cd = "JBody " + getVarName() + " = new JBody();\n";
        cd += body;
        appendCode(cd);

        String name = getAttrs().get("name");
        String el = "new JTag(\"" + name + "\")";
        if (!attrs.isEmpty()) {
            String attrsString = "";
            for (Map.Entry<String, String> entry : attrs.entrySet()) {
                if (entry.getValue() == null) {
                    attrsString += ".addAttr(\"" + entry.getKey() + "\", null)";
                } else {
                    attrsString += ".addAttr(\"" + entry.getKey() + "\", (String) " + parseExp(entry.getValue(), true) + ")";
                }

            }
            el += ".setAttrs(new JAttrs()" + attrsString + ")";
        }
        el += ".setjBody(" + getVarName() + ")";
        getParent().addElement(el);
    }



    public void addAttribute(String name, String value) {
        attrs.put(name, value);
    }

    @Override
    public void addElement(String element) {
        body += getVarName() + ".addElement(" + element + ");\n";
    }
}
