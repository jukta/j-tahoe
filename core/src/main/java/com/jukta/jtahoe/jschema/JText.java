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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JText jText = (JText) o;

        return text != null ? text.equals(jText.text) : jText.text == null;

    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }
}
