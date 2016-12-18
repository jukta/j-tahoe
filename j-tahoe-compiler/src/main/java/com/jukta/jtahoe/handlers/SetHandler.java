package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

/**
 * Created by Dmitriy Dobrovolskiy on 12.07.2016.
 *
 * @since *.*.*
 */
public class SetHandler extends AbstractHandler {
    public SetHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    private String element = null;

    @Override
    public void addElement(String element) {
        if (this.element != null) {
            throw new RuntimeException("Only one element is supported for set");
        }
        this.element = element;
    }

    @Override
    public void end() {
        String name = getAttrs().get("name");
        String value = getAttrs().get("value");
        String override = getAttrs().get("override");

        String exp;
        if (element == null && value != null) {
            value = parseExp(value, true);
            boolean isGlobal = "GLOBAL".equalsIgnoreCase(getAttrs().get("visibility"));

            if (!isGlobal) {
                exp = "attrs.set(\"" + name + "\", " + value + ");";
                if ("false".equals(override)) {
                    exp = "if (attrs.get(\"" + name + "\") == null) {" + exp + " }\n";
                }
            } else {
                exp = "attrs.setAttribute(\"" + name + "\", " + value + ");";
                if ("false".equals(override)) {
                    exp = "if (attrs.getAttribute(\"" + name + "\") == null) {" + exp + " }\n";
                }
            }
        } else if (element != null && value == null) {
            exp = "attrs.set(\"" + name + "\", " + element + ");";
            if ("false".equals(override)) {
                exp = "if (attrs.get(\"" + name + "\") == null) {" + exp + " }\n";
            }
        } else {
            throw new RuntimeException("Value or element should be defined in set");
        }
        getParent().appendCode(exp);
    }


    @Override
    public void appendCode(String code) {
        super.appendCode(code);
    }
}