package com.jukta.jtahoe.jschema;

import java.io.IOException;
import java.io.OutputStream;

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
    public String toHtml() {
        return text;
    }

    @Override
    public void toHtml(OutputStream outputStream) throws IOException {
        outputStream.write(text.getBytes());
    }

    @Override
    public String toString() {
        return toHtml();
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
