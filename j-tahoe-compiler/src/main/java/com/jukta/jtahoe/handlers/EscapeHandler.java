package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public class EscapeHandler extends AbstractHandler {

    public EscapeHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    @Override
    public void text(String text) {
        if (text.trim().equals("")) return;
        text = text.trim();
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        text = text.replaceAll("\n", "\\\\n");
        text = text.replaceAll("\r", "\\\\r");
        text = text.replaceAll("\"", "\\\\\"");
//        text = text.replaceAll("\\$", "\\$");
        addElement("new JText(\"" + text + "\")");
    }
}
