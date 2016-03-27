package com.jukta.jtahoe.jschema;

/**
 * Created by aleph on 18.02.2016.
 */
public class JTag implements JElement {
    private String name;
    private JAttrs attrs;
    private JBody jBody;

    public JTag(String name) {
        this.name = name;
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
    public String toJson() {
        String res = "{_name:\"" + name + "\"";
        if (attrs != null) res += "," + attrs.toJson();
        if (jBody != null) res += "," + jBody.toJson();
        res += "}";
        return res;
    }

    @Override
    public String toHtml() {
        String res = "<" + name;
        if (attrs != null) res += " " + attrs.toHtml();
        res += ">\n";
        if (jBody != null) res += jBody.toHtml();
        res += "</" + name + ">";
        return res;
    }
}
