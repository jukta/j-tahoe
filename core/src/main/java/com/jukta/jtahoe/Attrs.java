package com.jukta.jtahoe;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aleph on 17.02.2016.
 */
public class Attrs {
    private Map<String, Object> properties = new HashMap<String, Object>();
    private DataHandlerProvider dataHandlerProvider;

    public Attrs() {
    }

    public Attrs(DataHandlerProvider dataHandlerProvider) {
        this.dataHandlerProvider = dataHandlerProvider;
    }

    public Attrs set(String name, Object value) {
        properties.put(name, value);
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

    public void setDataHandlerProvider(DataHandlerProvider dataHandlerProvider) {
        this.dataHandlerProvider = dataHandlerProvider;
    }

    public DataHandlerProvider getDataHandlerProvider() {
        return dataHandlerProvider;
    }

}


