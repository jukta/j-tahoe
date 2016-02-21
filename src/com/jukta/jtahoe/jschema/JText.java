package com.jukta.jtahoe.jschema;

/**
 * Created by aleph on 18.02.2016.
 */
public class JText implements JElement {
    private String text;

    public JText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toJson() {
        return "\"" + text + "\"";
    }

    @Override
    public String toHtml() {
        return text;
    }
}
