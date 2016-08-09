package com.jukta.jtahoe.gen.model;

/**
 * @since 1.0
 */
public class TextNode implements Node {

    private String text;

    public TextNode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
