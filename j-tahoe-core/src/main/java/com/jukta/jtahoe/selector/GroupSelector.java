package com.jukta.jtahoe.selector;

import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;
import com.jukta.jtahoe.jschema.JTag;

import java.util.*;

/**
 * Created by Sergey on 12/7/2017.
 */
public class GroupSelector {
    private TagSelector selector;

    private GroupSelector(TagSelector selector) {
        this.selector = selector;
    }

    public Set<JTag> select(JElement t) {
        JTag root = new JTag("").setjBody(new JBody().addElement(t));
        return new HashSet<>(apply(null, root, selector));
    }

    private List<JTag> apply(JTag parent, JTag tag, TagSelector selector) {
        List<JTag> res = new ArrayList<>();
        if (">".equals(selector.getPr())) {
            res.addAll(children(tag, selector));
        } else if ("+".equals(selector.getPr())) {
            boolean bf = true;
            if (parent == null) return new ArrayList<>();
            for (JTag ch : getChildTags(parent.getjBody())) {
                if (ch.equals(tag)) {
                    bf = false;
                    continue;
                }
                if (bf) continue;
                if (selector.select(ch)) {
                    TagSelector next = selector.getNext();
                    if (next == null)
                        return Collections.singletonList(ch);
                    return apply(parent, ch, next);
                }
            }
            return new ArrayList<>();
        } if ("~".equals(selector.getPr())) {
            boolean bf = true;
            List<JTag> res1 = new ArrayList<>();
            if (parent == null) return res1;
            for (JTag ch : getChildTags(parent.getjBody())) {
                if (ch.equals(tag)) {
                    bf = false;
                    continue;
                }
                if (bf) continue;
                if (selector.select(ch)) {
                    TagSelector next = selector.getNext();
                    if (next == null) {
                        res1.add(ch);
                    } else {
                        res1.addAll(apply(parent, ch, next));
                    }
                }
            }
            return res1;
        } else {
            res.addAll(trav(tag, selector));
        }

        TagSelector next = selector.getNext();
        if (next == null) return res;

        List<JTag> res1 = new ArrayList<>();
        for (JTag jt : res) {
            res1.addAll(apply(tag, jt, next));
        }
        return res1;
    }

    private List<JTag> children(JTag parent, TagSelector tagSelector) {
        List<JTag> l = new ArrayList<>();
        for (JTag t : getChildTags(parent.getjBody())) {
            if (tagSelector.select(t)) {
                l.add(t);
            }
        }
        return l;
    }

    private List<JTag> trav(JTag parent, TagSelector tagSelector) {
        List<JTag> l = new ArrayList<>();
        for (JTag t : getChildTags(parent.getjBody())) {
            if (tagSelector.select(t)) {
                l.add(t);
            }
            l.addAll(trav(t, tagSelector));
        }
        return l;
    }

    private List<JTag> getChildTags(JElement jElement) {
        List<JTag> l = new ArrayList<>();
        if (jElement instanceof JTag) {
            l.add((JTag) jElement);
        } else if (jElement instanceof JBody) {
            for (JElement el : ((JBody) jElement).getElements()) {
                List<JTag> l1 = getChildTags(el);
                if (!l1.isEmpty()) {
                    l.addAll(l1);
                }
            }
        }
        return l;
    }

    public static GroupSelector parse(String sel) {
        TagSelector selector = null;
        TagSelector lastSelector = null;
        for (String s : sel.split(">")) {
            if (!sel.startsWith(s)) s = ">"+s.trim();
            for (String s1 : s.split("\\+")) {
                if (!s.startsWith(s1)) s1 = "+" + s1.trim();
                for (String s2 : s1.split("\\~")) {
                    if (!s1.startsWith(s2)) s2 = "~" + s2.trim();
                    for (String s3 : s2.trim().split("\\s+")) {
                        TagSelector ts = TagSelector.parse(s3);
                        if (selector == null) {
                            selector = ts;
                        } else {
                            lastSelector.setNext(ts);
                        }
                        lastSelector = ts;
                    }
                }
            }
        }
        return new GroupSelector(selector);
    }


}
