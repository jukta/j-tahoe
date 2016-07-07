package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;

import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public class ParentHandler extends AbstractHandler {

    public ParentHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
    }

    @Override
    public void end() {
        String el = "super." + getPar(getParent()) + "(attrs)";
        getParent().addElement(el);

    }

    private String getPar(AbstractHandler handler) {
        if (handler instanceof BlockHandler) return "body";
        if (handler instanceof DefHandler) return "def_" + ((DefHandler) handler).getDefName();
        else return getPar(handler.getParent());
    }

}
