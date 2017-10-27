package com.jukta.jtahoe.jschema;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @since 1.0
 */
public class JTagTest {

    @Test
    public void toHtml() {
        JTag tag = new JTag("a").setAttrs(new JAttrs().addAttr("attr1", "val1").addAttr("attr2", "val2")).setjBody(new JBody().addElement(
                new JTag("b").setAttrs(new JAttrs().addAttr("attr3", "val3")).setjBody(new JBody().addElement(new JText("text1")).addElement(new JText("text1")))
        ));
        Assert.assertEquals("<a attr2=\"val2\" attr1=\"val1\">" +
                "<b attr3=\"val3\">" +
                "text1" +
                "text1" +
                "</b>" +
                "</a>", tag.toHtml());
    }

    @Test
    public void toHtmlStream() throws IOException {
        JTag tag = new JTag("a").setAttrs(new JAttrs().addAttr("attr1", "val1").addAttr("attr2", "val2")).setjBody(new JBody().addElement(
                new JTag("b").setAttrs(new JAttrs().addAttr("attr3", "val3")).setjBody(new JBody().addElement(new JText("text1")).addElement(new JText("text1")))
        ));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tag.toHtml(baos);

        Assert.assertEquals("<a attr2=\"val2\" attr1=\"val1\">" +
                "<b attr3=\"val3\">" +
                "text1" +
                "text1" +
                "</b>" +
                "</a>", baos.toString());
    }

    @Test
    public void selfClosing() {
        JTag tag = new JTag("a").setAttrs(new JAttrs().addAttr("attr1", "val1"));
        Assert.assertEquals("<a attr1=\"val1\"/>", tag.toHtml());
    }

}
