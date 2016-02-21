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
}
