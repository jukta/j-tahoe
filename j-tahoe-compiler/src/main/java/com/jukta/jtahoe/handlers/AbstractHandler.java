package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public abstract class AbstractHandler {

    private AbstractHandler parent;
    protected GenContext genContext;

    private NamedNode node;

    private static int seq = 0;
    private int $seq = 0;

    protected AbstractHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        this.parent = parent;
        this.genContext = genContext;
        this.node = node;
        $seq = seq++;
    }

    public void start() {

    }

    public void text(String text) {
        text = text.trim();
        if (text.equals("")) return;
        text = text.replace("\n", "");
        text = parseExp(text, true);
        addElement("new JText(" + text + ".toString())");
    }

    public void end() {

    }

    public int getSeq() {
        return $seq;
    }

    public AbstractHandler getParent() {
        return parent;
    }

    public String getName() {
        return node.getName();
    }

    public Map<String, String> getAttrs() {
        return node.getAttributes();
    }

    public NamedNode getNode() {
        return node;
    }

    public void appendCode(String code) {
        getParent().appendCode(code);
    }

    public void addElement(String element) {
        getParent().addElement(element);
    }

    public String parseExp(String str, boolean wrap) {
        if (str == null) {
            return null;
        }
        str = str.replaceAll("\\$\\{", "#{");
        return "eval(attrs, \"" + str + "\")";
    }

    public String parseItExp(String str, boolean wrap) {
        if (str == null) {
            return "Collections.emptyList()";
        }
        str = str.replaceAll("\\$\\{", "#{");
        return "evalIt(attrs, \"" + str + "\")";
    }

    public BlockHandler getBlock() {
        return getBlock(getParent());
    }

    private BlockHandler getBlock(AbstractHandler handler) {
        if (handler instanceof BlockHandler) return (BlockHandler) handler;
        else return getBlock(handler.getParent());
    }

    public GenContext getGenContext() {
        return genContext;
    }

    public String getCurPackage() {
        return getPackage(genContext.getCurrentNamespace());
    }

    public String getPackage(String namespace) {
        if (".".equals(namespace)) {
            namespace = genContext.getCurrentNamespace();
        }

        String relPath = null;
        try {
            relPath = new URI(namespace).getRawPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String pack = relPath.replaceAll("/", ".");
        if (pack.startsWith(".")) pack = pack.substring(1);
        if (pack.endsWith(".")) pack = pack.substring(0, pack.length() - 1);
        return pack;
    }

    public String getVarName() {
        return "__" + $seq;
    }

    protected String processPrefix(String name) {
        String[] sp = name.split(":");
        if (sp.length < 2) return name;
        String prefix = sp[0];
        prefix = getGenContext().getPrefixes().get(prefix);
        if (prefix != null) {
            return getPackage(prefix) + "." + sp[1];
        } else {
            return sp[1];
        }
    }
}
