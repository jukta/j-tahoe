package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

/**
 * @since 1.0.1
 */
public class UnsetHandler extends AbstractHandler {
    public UnsetHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    private String element = null;

    @Override
    public void addElement(String element) {
        if (this.element != null) {
            throw new RuntimeException("Only one element is supported for unset");
        }
        this.element = element;
    }

    @Override
    public void end() {
        String name = getAttrs().get("name");

        String exp;
        boolean isGlobal = "GLOBAL".equalsIgnoreCase(getAttrs().get("visibility"));
        if (!isGlobal) {
            exp = "attrs.unset(\"" + name + "\");";
        } else {
            exp = "attrs.unsetAttribute(\"" + name + "\");";
        }
        getParent().appendCode(exp);
    }


    @Override
    public void appendCode(String code) {
        super.appendCode(code);
    }
}