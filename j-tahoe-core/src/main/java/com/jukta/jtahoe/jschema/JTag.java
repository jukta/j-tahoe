package com.jukta.jtahoe.jschema;

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
    public String toJson() {
        String res = "{\"_name\":\"" + name + "\"";
        if (attrs != null && !attrs.isEmpty()) res += "," + attrs.toJson();
        if (jBody != null) {
            String b = jBody.toJson();
            if (!"".equals(b)) res += "," + b;
        }
        res += "}";
        return res;
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
