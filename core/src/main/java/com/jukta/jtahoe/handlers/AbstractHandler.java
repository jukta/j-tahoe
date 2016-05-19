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
    private String varName;
    private static int seq = 0;

    protected AbstractHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        this.name = name;
        this.attrs = attrs;
        this.parent = parent;
        this.genContext = genContext;
        varName = "__" + seq++;
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

    public String parseExp(String str, boolean wrap) {
        Pattern pattern = Pattern.compile("\\$\\{([^\\}]*)\\}");
        Matcher m = pattern.matcher(str);
        if (m.find()) {
            if (wrap) {
                String match = m.group(1);
                boolean hitStart = m.start() > 0;
                boolean hitEnd = m.end() < str.length();

                String rep = "eval(attrs, \"attrs." + match + "\")";
                if (hitStart) {
                    rep = "\" + " + rep;
                }
                if (hitEnd) {
                    rep = rep + " + \"";
                }

                String str1 = m.replaceFirst(rep);
                if (hitStart) {
                    str1 = "\"" + str1;
                }
                if (hitEnd) {
                    str1 = str1 + "\"";
                }
                str = str1;
            } else {
                str = m.replaceFirst("eval(attrs, \"attrs." + m.group(1) + "\")");
            }

        } else if (wrap) {
            str = "\"" + str + "\"";
        }
        return str;
    }

    public String parseItExp(String str, boolean wrap) {
        Pattern pattern = Pattern.compile("\\$\\{([^\\}]*)\\}");
        Matcher m = pattern.matcher(str);
        if (m.find()) {
            if (wrap) {
                str = m.replaceFirst("\" + evalIt(attrs, \"attrs." + m.group(1) + "\") + \"");
            } else {
                str = m.replaceFirst("evalIt(attrs, \"attrs." + m.group(1) + "\")");
            }
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
        String relPath = genContext.getRootDir().toURI().relativize(genContext.getCurrentFile().getParentUri()).getPath();
        String pack = relPath.replaceAll("/", ".");
        if (pack.endsWith(".")) pack = pack.substring(0, pack.length()-1);
        return pack;
    }

    public String getVarName() {
        return varName;
    }
}
