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

    public void addAttr(String name, String value) {
        attrs.put(name, value);
    }

    public String toJson() {
        return "";
    }

    public String toHtml() {
        return "";
    }
}
