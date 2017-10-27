package com.jukta.jtahoe.gen.xml;

import com.jukta.jtahoe.gen.model.NamedNode;
import com.jukta.jtahoe.gen.model.TextNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by aleph on 26.03.2016.
 */
public class FileHandler implements BasicParser.Handler {


    public static final String XHTML = "http://www.w3.org/1999/xhtml";
    private Stack<NamedNode> stack = new Stack<>();
    private NamedNode node;
    private Map<String, String> prefixes = new HashMap<>();
    private BasicParser parser;

    public void setParser(BasicParser parser) {
        this.parser = parser;
    }

    public NamedNode getNode() {
        return node;
    }

    public NamedNode getTop() {
        if (stack.empty()) return null;
        return stack.peek();
    }

    public void start(String ns, String name, Map<String, String> attrs, boolean selfClosing) {

        for (Map.Entry<String, String> attribute : attrs.entrySet()) {
            if (attribute.getKey().startsWith("xmlns:")) {
                prefixes.put(attribute.getKey().substring(6), attribute.getValue());
            }
        }
        NamedNode parent = null;
        if (!stack.isEmpty()) {
            parent = stack.peek();
        }

        String uri = getUri(ns, name);

        NamedNode node = new NamedNode(uri, name, attrs, parent);
        node.getPrefixes().putAll(prefixes);
//        prefixes.clear();
        if (name.equals("escape") && uri.equals("http://jukta.com/tahoe/schema")) {
            parser.setBypass(true);
            thNS = ns;
        }
        if (parent == null) {
            this.node = node;
        } else {
            parent.getChildren().add(node);
        }
        node.setSelfClosing(selfClosing);
        if (!selfClosing) stack.push(node);
    }

    private String thNS;

    public void end(String ns, String name) {
        stack.pop();
    }

    @Override
    public void text(String text) {
        if (stack.empty()) return;
        if (text.equals("</" + thNS + ":escape>")) {
            parser.setBypass(false);
            end(thNS, "escape");
        } else {
            stack.peek().getChildren().add(new TextNode(text));
        }
    }

    private String getUri(String ns, String name) {
        String uri = "__html__";
        if (ns != null) {
            uri = prefixes.get(ns);
        }

        if (uri.equals(XHTML)) uri = "__html__";
        return uri;
    }

}
