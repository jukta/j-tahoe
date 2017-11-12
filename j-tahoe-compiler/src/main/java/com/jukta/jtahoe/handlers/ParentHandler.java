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
//        DefHandler defHandler = getPar(getParent());
//        if (defHandler.getBlock(false) instanceof FuncHandler) {
//            String el = "parent(attrs, \"" + defHandler.getDefName() + "\", this)";
//            getParent().addElement(el);
//            defHandler.addSuperMethod(defHandler.getDefName());
//        } else if (defHandler.name == null) {
//            String el = "super.def_" + defHandler.getBlock(true).getParentBlock() + "(attrs)";
//            getParent().addElement(el);
//        } else {
//            String el = "super." + defHandler.getDefName() + "(attrs)";
//            getParent().addElement(el);
//        }

        String el = "parent(attrs, null)";
        getParent().addElement(el);

    }

    private DefHandler getPar(AbstractHandler handler) {
        if (handler instanceof BlockHandler) {
            DefHandler defHandler = ((BlockHandler) handler).defHandler;
            if (defHandler != null) return defHandler;
            return null;
//            return "def_" + getBlock(false).getBlockName();
        } else if (handler instanceof DefHandler) {
            return ((DefHandler) handler);
        } else return getPar(handler.getParent());

    }

}
