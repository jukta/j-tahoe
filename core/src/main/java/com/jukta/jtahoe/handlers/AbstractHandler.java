package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.GenContext;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aleph on 18.02.2016.
 */
public abstract class AbstractHandler {

    private AbstractHandler parent;
    private String name;
    private Map<String, String> attrs;
    protected GenContext genContext;

    protected AbstractHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        this.name = name;
        this.attrs = attrs;
        this.parent = parent;
        this.genContext = genContext;
    }

    public void start() {

    }

    public void text(String text) {
        text = text.trim();
        if (text.equals("")) return;
        text = text.replace("\n", "");
        text = parseExp(text);
        addElement("new JText(\"" + text + "\")");
    }

    public void end() {

    }

    public AbstractHandler getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void appendCode(String code) {
        getParent().appendCode(code);
    }

    public void addElement(String element) {
        getParent().addElement(element);
    }

    public String parseExp(String str) {
        Pattern pattern = Pattern.compile("\\$\\{([^\\}]*)\\}");
        Matcher m = pattern.matcher(str);
        if (m.find()) {
            str = m.replaceFirst("\" + eval(attrs, \"attrs." + m.group(1) + "\") + \"");
//            str = "\"" + str + "\"";
        }
        return str;
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
        String relPath = genContext.getRootDir().toURI().relativize(genContext.getCurrentFile().getParentFile().toURI()).getPath();
        String pack = relPath.replaceAll("/", ".");
        if (pack.endsWith(".")) pack = pack.substring(0, pack.length()-1);
        return pack;
    }
}
