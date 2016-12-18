package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

/**
 * Created by aleph on 18.02.2016.
 */
public class ParentHandler extends AbstractHandler {

    public ParentHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    @Override
    public void end() {
        String el = "super." + getPar(getParent()) + "(attrs)";
        getParent().addElement(el);

    }

    private String getPar(AbstractHandler handler) {
        if (handler instanceof BlockHandler) {
            if (handler.getAttrs().get("parent") != null) {
                return "def_";
            }
        }
        if (handler instanceof DefHandler) {
            String defName = ((DefHandler) handler).getDefName();
            if (defName == null) return "def_";
            return "def_" + defName;
        }
        else return getPar(handler.getParent());
    }

}
