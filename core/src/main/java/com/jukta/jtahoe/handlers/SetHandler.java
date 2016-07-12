package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;

import java.util.Map;

/**
 * Created by Dmitriy Dobrovolskiy on 12.07.2016.
 *
 * @since *.*.*
 */
public class SetHandler extends AbstractHandler {
    public SetHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
    }

    @Override
    public void end() {
        String name = getAttrs().get("name");
        String value = parseExp(getAttrs().get("value"), true);
        String override = getAttrs().get("override");
        boolean isGlobal = "GLOBAL".equalsIgnoreCase(getAttrs().get("visibility"));

        String exp;
        if (!isGlobal) {
            exp = "attrs.set(\"" + name + "\", " + value + ");";
            if ("false".equals(override)) {
                exp = "if (attrs.get(\"" + name + "\") == null) {" + exp + " }";
            }
        } else {
            exp = "attrs.setAttribute(\"" + name + "\", " + value + ");";
            if ("false".equals(override)) {
                exp = "if (attrs.getAttribute(\"" + name + "\") == null) {" + exp + " }";
            }
        }
        getParent().appendCode(exp);
    }


    @Override
    public void appendCode(String code) {
        super.appendCode(code);
    }
}