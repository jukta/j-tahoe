package com.jukta.jtahoe;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aleph on 17.02.2016.
 */
public class Attrs {
    private Map<String, Object> properties = new HashMap<String, Object>();
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private DataHandlerProvider dataHandlerProvider;

    public Attrs() {
    }

    public Attrs(Attrs attrs) {
        dataHandlerProvider = attrs.dataHandlerProvider;
        attributes = attrs.attributes;
    }

    public Attrs set(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    public Attrs unset(String name) {
        properties.remove(name);
        return this;
    }

    public Attrs setAttribute(String name, Object value) {
        attributes.put(name, value);
        return this;
    }

    public Attrs unsetAttribute(String name) {
        attributes.remove(name);
        return this;
    }


    public Attrs setAll(Attrs attrs) {
        for (Map.Entry<String, Object> attr : attrs.properties.entrySet()) {
            attrs.set(attr.getKey(), attr.getValue());
        }
        return this;
    }

    public Object get(String name) {
        return properties.get(name);
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void setDataHandlerProvider(DataHandlerProvider dataHandlerProvider) {
        this.dataHandlerProvider = dataHandlerProvider;
    }

    public DataHandlerProvider getDataHandlerProvider() {
        return dataHandlerProvider;
    }

}


