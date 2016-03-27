package com.jukta.jtahoe;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aleph on 17.02.2016.
 */
public class Attrs {
    private Map<String, Object> properties = new HashMap<String, Object>();

    public Attrs set(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    public Object get(String name) {
        return properties.get(name);
    }

}


