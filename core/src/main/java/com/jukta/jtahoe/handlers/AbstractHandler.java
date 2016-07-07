package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;

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

    private static int seq = 0;
    private int $seq = 0;

    public static final Pattern EL_EXP_PATTERN = Pattern.compile("\\$\\{([^\\}]*)\\}");
    public static final Pattern VARIABLE_EXP_PATTERN = Pattern.compile("([a-zA-Z_$][a-zA-Z_$0-9]*(\\.[a-zA-Z_$][a-zA-Z_$0-9]*)*)");

    protected AbstractHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        this.name = name;
        this.attrs = attrs;
        this.parent = parent;
        this.genContext = genContext;
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
        Matcher m = EL_EXP_PATTERN.matcher(str);
        if (m.find()) {
            if (wrap) {
                String match = m.group(1);
                boolean hitStart = m.start() > 0;
                boolean hitEnd = m.end() < str.length();

                Matcher varMatcher = VARIABLE_EXP_PATTERN.matcher(match);
                int shift = 0;
                while (varMatcher.find()) {
                    match = new StringBuilder(match).insert(varMatcher.start() + shift, "attrs.").toString();
                    shift += 6;
                }

                String rep = "eval(attrs, \"" + match + "\")";
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
        Matcher m = EL_EXP_PATTERN.matcher(str);
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
            return prefix + "." + sp[1];
        } else {
            return sp[1];
        }
    }
}
