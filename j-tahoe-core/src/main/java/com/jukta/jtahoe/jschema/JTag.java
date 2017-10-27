package com.jukta.jtahoe.jschema;

import java.io.IOException;
import java.io.OutputStream;

public class JTag implements JElement {
    private String name;
    private JAttrs attrs;
    private JBody jBody;

    public JTag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public JAttrs getAttrs() {
        return attrs;
    }

    public JTag setAttrs(JAttrs attrs) {
        this.attrs = attrs;
        return this;
    }

    public JTag setjBody(JBody jBody) {
        this.jBody = jBody;
        return this;
    }

    public JBody getjBody() {
        return jBody;
    }

    @Override
    public String toHtml() {
        String res = "<" + name;
        if (attrs != null && !attrs.isEmpty()) res += " " + attrs.toHtml();
        if (jBody == null) {
            res += "/>";
        } else {
            res += ">";
            res += jBody.toHtml();
            res += "</" + name + ">";
        }
        return res;
    }

    @Override
    public void toHtml(OutputStream outputStream) throws IOException {
        outputStream.write(("<" + name).getBytes());
        if (attrs != null && !attrs.isEmpty()) {
            outputStream.write(" ".getBytes());
            attrs.toHtml(outputStream);
        }
        if (jBody == null) {
            outputStream.write("/>".getBytes());
        } else {
            outputStream.write(">".getBytes());
            jBody.toHtml(outputStream);
            outputStream.write(("</" + name + ">").getBytes());
        }
    }

    @Override
    public String toString() {
        return toHtml();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JTag jTag = (JTag) o;

        if (name != null ? !name.equals(jTag.name) : jTag.name != null) return false;
        if (attrs != null ? !attrs.equals(jTag.attrs) : jTag.attrs != null) return false;
        return jBody != null ? jBody.equals(jTag.jBody) : jTag.jBody == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (attrs != null ? attrs.hashCode() : 0);
        result = 31 * result + (jBody != null ? jBody.hashCode() : 0);
        return result;
    }
}
