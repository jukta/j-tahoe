package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aleph on 18.02.2016.
 */
public abstract class AbstractHandler {

    private AbstractHandler parent;
    protected GenContext genContext;

    private NamedNode node;

    private static int seq = 0;
    private int $seq = 0;

    public static final Pattern EL_EXP_PATTERN = Pattern.compile("\\$\\{([^\\}]*)\\}");
    public static final Pattern VARIABLE_EXP_PATTERN = Pattern.compile("[a-zA-Z_$][a-zA-Z_$0-9]*(\\.[a-zA-Z_$][a-zA-Z_$0-9]*)*\\s*(?=([^\']*'[^\']*')*[^\']*$)");

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
        Matcher m = EL_EXP_PATTERN.matcher(str);
        if (m.find()) {
            int minSt = Integer.MAX_VALUE;
            int maxEnd = Integer.MIN_VALUE;
            boolean hMinSt = false;
            boolean hMaxEnd = false;
            do {
                if (wrap) {
                    String match = m.group(1);
                    minSt = Math.min(minSt, m.start());
                    maxEnd = Math.max(maxEnd, m.end());
                    hMinSt = minSt > 0;
                    hMaxEnd = maxEnd < str.length();

                    Matcher varMatcher = VARIABLE_EXP_PATTERN.matcher(match);
                    int shift = 0;
                    while (varMatcher.find()) {
                        match = new StringBuilder(match).insert(varMatcher.start() + shift, "attrs.").toString();
                        shift += 6;
                    }

                    String rep = "eval(attrs, \"" + match + "\")";
                    if (m.start() > 0) {
                        rep = "\" + " + rep;
                    }
                    if (m.end() < str.length()) {
                        rep = rep + " + \"";
                    }
                    str = m.replaceFirst(rep);
                } else {
                    str = m.replaceFirst("eval(attrs, \"attrs." + m.group(1) + "\")");
                }
                m = EL_EXP_PATTERN.matcher(str);
            } while (m.find());
            if (hMinSt) {
                str = "\"" + str;
            }
            if (hMaxEnd) {
                str = str + "\"";
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

//        String relPath = node.getNamespace().replaceAll("/", ".");
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
