package com.jukta.jtahoe.jschema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleph on 18.02.2016.
 */
public class JBody implements JElement {
    private List<JElement> elements = new ArrayList<JElement>();

    public JBody addElement(JElement element) {
        elements.add(element);
        return this;
    }

    @Override
    public String toJson() {
        String res = "{_: [";
        for (JElement element : elements) {
            res += "," + element.toJson();
        }
        res += "]";
        return res;
    }

    @Override
    public String toHtml() {
        String res = "";
        for (JElement element : elements) {
            res += element.toHtml() + "\n";
        }
        return res;
    }

    @Override
    public String toString() {
        return toJson();
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
