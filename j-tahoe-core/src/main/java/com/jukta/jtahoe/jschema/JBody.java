package com.jukta.jtahoe.jschema;

import java.util.ArrayList;
import java.util.List;

public class JBody implements JElement {
    private List<JElement> elements = new ArrayList<JElement>();

    public JBody addElement(JElement element) {
        elements.add(element);
        return this;
    }

    public List<JElement> getElements() {
        return elements;
    }

    private String toJson1() {
        List<String> eList = new ArrayList<>();
        for (JElement element : elements) {
            String e = (element instanceof JBody) ? ((JBody) element).toJson1() : element.toJson();
            if (e != null && !"".equals(e)) eList.add(e);
        }
        return String.join(",", eList);
    }

    @Override
    public String toJson() {
        if (elements.isEmpty()) {
            return "";
        }
        String res = "\"_\": [";
        res += toJson1();
        res += "]";
        return res;
    }

    @Override
    public String toHtml() {
        String res = "";
        for (JElement element : elements) {
            res += element.toHtml();
        }
        return res;
    }

    @Override
    public String toString() {
        return toHtml();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JBody jBody = (JBody) o;

        return elements != null ? elements.equals(jBody.elements) : jBody.elements == null;

    }

    @Override
    public int hashCode() {
        return elements != null ? elements.hashCode() : 0;
    }
}
