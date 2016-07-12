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

    String body = "";

    @Override
    public void end() {
        String name = getAttrs().get("name");
        String value = parseExp(getAttrs().get("value"), true);
        String override = getAttrs().get("override");
        String visibility = getAttrs().get("visibility");

        String exp = "attrs.set(\"" + name + "\", " + value + ");";
        if ("false".equals(override)) {
            exp = "if (attrs.get(\"" + name + "\") == null) {" + exp + " }";
        }
        getParent().appendCode(exp);
    }


    @Override
    public void appendCode(String code) {
        super.appendCode(code);
    }

    private enum Visibility {
        GLOBAL,
        BLOCK,
        LOCAL
    }
}