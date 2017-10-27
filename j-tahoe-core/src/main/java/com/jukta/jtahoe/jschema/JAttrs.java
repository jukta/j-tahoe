package com.jukta.jtahoe.jschema;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public String toHtml() {
        String res = "";
        if (!isEmpty()) {
            int i = 0;
            for (Map.Entry entry : attrs.entrySet()) {
                if (i++ > 0) res += " ";
                res += entry.getKey();
                if (entry.getValue() != null) res += "=\"" + entry.getValue() + "\"";
            }
        }
        return res;
    }

    public void toHtml(OutputStream outputStream) throws IOException {
        outputStream.write(toHtml().getBytes());
    }

    public boolean isEmpty() {
        return attrs.isEmpty();
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
