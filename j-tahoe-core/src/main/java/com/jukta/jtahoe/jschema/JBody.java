package com.jukta.jtahoe.jschema;

import java.io.IOException;
import java.io.OutputStream;
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

    @Override
    public String toHtml() {
        String res = "";
        for (JElement element : elements) {
            res += element.toHtml();
        }
        return res;
    }

    @Override
    public void toHtml(OutputStream outputStream) throws IOException {
        for (JElement element : elements) {
            element.toHtml(outputStream);
        }
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
