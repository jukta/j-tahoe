package com.jukta.jtahoe.selector;

import com.jukta.jtahoe.jschema.JAttrs;
import com.jukta.jtahoe.jschema.JTag;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sergey on 12/7/2017.
 */
public class TagSelector {

    private String sel;
    private List<String> classes = new ArrayList<>();
    private String id;
    private Map<String, String> attrs = new HashMap<>();
    private String pr;
    private TagSelector next;

    public TagSelector getNext() {
        return next;
    }

    public void setNext(TagSelector next) {
        this.next = next;
    }

    public boolean select(JTag element) {
        if (sel != null && !"*".equals(sel) && !element.getName().equals(sel)) {
            return false;
        }
        if (!classes.isEmpty()) {
            JAttrs jAttrs = element.getAttrs();
            if (jAttrs == null || jAttrs.getAttrs() == null) return false;
            String cls = jAttrs.getAttrs().get("class");
            if (cls == null) return false;
            for (String cl : classes) {
                if (!cls.contains(cl)) return false;
            }

        }
        return true;
    }

    public String getPr() {
        return pr;
    }

    public static TagSelector parse(String selector) {
        TagSelector ts = new TagSelector();
        String delim = ".#[";
        String type = selector.substring(0,1);
        if (">+~".contains(type)) {
            ts.pr = type;
            selector = selector.substring(1);
        }
        StringTokenizer tokenizer = new StringTokenizer(selector, delim, true);


        String prev = null;
        while (tokenizer.hasMoreTokens()) {
            String t = tokenizer.nextToken();
            if (delim.contains(t)) {
                prev = t;
            } else {
                if (".".equals(prev)) {
                    ts.classes.add(t);
                } else if ("#".equals(prev)) {
                    ts.id = t;
                } else if ("[".equals(prev)) {
                    Pattern p = Pattern.compile("(\\w+)\\W+(\\w+)");
                    Matcher m = p.matcher(t);
                    if (m.find()) {
                        ts.attrs.put(m.group(1), m.group(2));
                    }
                } else {
                    ts.sel = t;
                }
                prev = null;
            }
        }

        return ts;
    }

    public static void main(String[] args) {
        TagSelector selector = TagSelector.parse("*[data='hekfe']");
        System.out.println();
    }
}
