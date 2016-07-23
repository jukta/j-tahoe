package com.jukta.jtahoe.model;

import com.jukta.jtahoe.BlockModelProvider;
import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.handlers.*;

import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.0
 */
public class NodeProcessor {

    public static final String JTAHOE_CORE_URI = "http://svsoft.net/tahoe/schema";

    private void process(Node node, GenContext context, AbstractHandler pHandler) {
        if (node instanceof TextNode) {
            pHandler.text(((TextNode) node).getText());
        } else {
            NamedNode namedNode = (NamedNode) node;
            AbstractHandler handler = createHandler(namedNode, context, pHandler);
            handler.start();
            for (Node cNode : namedNode.getChildren()) {
                process(cNode, context, handler);
            }
            handler.end();
        }
    }

    public List<JavaFileObject> process(BlockModelProvider blockModelProvider) {
        List<JavaFileObject> fileObjectList = new ArrayList<>();
        GenContext context = new GenContext(fileObjectList);
        for (NamedNode node : blockModelProvider) {
            process(node, context);
        }
        return fileObjectList;
    }

    public void process(NamedNode node, GenContext context) {
        process(node, context, null);
    }

    private AbstractHandler createHandler(NamedNode node, GenContext genContext, AbstractHandler parent) {
        AbstractHandler handler = null;
        if (JTAHOE_CORE_URI.equals(node.getNamespace())) {
            if (node.getName().equals("root")) {
                handler = new RootHandler(genContext, node, parent);
            } else if (node.getName().equals("block")) {
                handler = new BlockHandler(genContext, node, parent);
            } else if (node.getName().startsWith("def")) {
                handler = new DefHandler(genContext, node, parent);
            } else if (node.getName().startsWith("parent")) {
                handler = new ParentHandler(genContext, node, parent);
            } else if (node.getName().startsWith("for")) {
                handler = new ForHandler(genContext, node, parent);
            } else if (node.getName().startsWith("if")) {
                handler = new IfHandler(genContext, node, parent);
            } else if (node.getName().startsWith("tag")) {
                handler = new TagHandler(genContext, node, parent);
            } else if (node.getName().startsWith("include")) {
                handler = new IncludeHandler(genContext, node, parent);
            } else if (node.getName().startsWith("set")) {
                handler = new SetHandler(genContext, node, parent);
            }
        } else {
            if (!isHtml(node)) {
                handler = new FuncHandler(genContext, node, parent);
            } else {
                handler = new HtmlHandler(genContext, node, parent);
            }
        }
        return handler;
    }

    boolean isHtml(NamedNode node) {
        return "__html__".equals(node.getNamespace());
    }

}
