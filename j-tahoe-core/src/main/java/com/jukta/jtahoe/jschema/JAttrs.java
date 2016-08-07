package com.jukta.jtahoe.jschema;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public class JAttrs {
    private Map<String, String> attrs = new HashMap<String, String>();

    public JAttrs() {
    }

    public JAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    public JAttrs addAttr(String name, String value) {
        attrs.put(name, value);
        return this;
    }

    public String toJson() {
//        todo implement
        return "";
    }

    public String toHtml() {
        String res = "";
        for (Map.Entry entry : attrs.entrySet()) {
            res += entry.getKey();
            if (entry.getValue() != null) res += "=\"" + entry.getValue() + "\"";
            res += " ";
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JAttrs jAttrs = (JAttrs) o;

        return attrs != null ? attrs.equals(jAttrs.attrs) : jAttrs.attrs == null;

    }

    @Override
    public int hashCode() {
        return attrs != null ? attrs.hashCode() : 0;
    }
}
