package com.jukta.jtahoe.gen;

import com.jukta.jtahoe.BlockModelProvider;
import com.jukta.jtahoe.gen.model.NamedNode;
import com.jukta.jtahoe.gen.model.Node;
import com.jukta.jtahoe.gen.model.TextNode;
import com.jukta.jtahoe.handlers.*;

import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.0
 */
public class NodeProcessor {

    public static final String JTAHOE_CORE_URI = "http://jukta.com/tahoe/schema";

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
            } else if (node.getName().equals("def")) {
                handler = new DefHandler(genContext, node, parent);
            } else if (node.getName().equals("parent")) {
                handler = new ParentHandler(genContext, node, parent);
            } else if (node.getName().equals("for")) {
                handler = new ForHandler(genContext, node, parent);
            } else if (node.getName().equals("if")) {
                handler = new IfHandler(genContext, node, parent);
            } else if (node.getName().equals("tag")) {
                handler = new TagHandler(genContext, node, parent);
            } else if (node.getName().equals("tagAttr")) {
                handler = new TagAttrHandler(genContext, node, parent);
            } else if (node.getName().equals("include")) {
                handler = new IncludeHandler(genContext, node, parent);
            } else if (node.getName().equals("set")) {
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
