package com.jukta.jtahoe.gen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 1.0
 */
public class NamedNode implements Node {
    private String namespace;
    private String name;
    private List<Node> children = new ArrayList<>();
    private Map<String, String> attributes;
    private NamedNode parent;
    private Map<String, String> prefixes = new HashMap<>();

    public NamedNode(String namespace, String name, Map<String, String> attributes, NamedNode parent) {
        this.namespace = namespace;
        this.name = name;
        this.attributes = attributes;
        this.parent = parent;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public NamedNode getParent() {
        return parent;
    }

    public Map<String, String> getPrefixes() {
        return prefixes;
    }

    @Override
    public String toString() {
        return namespace + ":" + name + "[" + attributes.toString() + "]";
    }
}
