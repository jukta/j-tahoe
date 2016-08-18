package com.jukta.jtahoe.jschema;

import org.junit.Assert;
import org.junit.Test;

/**
 * @since 1.0
 */
public class JTagTest {

    @Test
    public void toJson() {
        JTag tag = new JTag("a").setAttrs(new JAttrs().addAttr("attr1", "val1").addAttr("attr2", "val2")).setjBody(new JBody().addElement(
                new JTag("b").setAttrs(new JAttrs().addAttr("attr3", "val3")).setjBody(new JBody().addElement(new JText("text1")).addElement(new JText("text1")))
        ));
        Assert.assertEquals("{_name:\"a\",_attrs: {attr2:\"val2\",attr1:\"val1\"},_: [{_name:\"b\",_attrs: {attr3:\"val3\"},_: [\"text1\",\"text1\"]}]}", tag.toJson());
    }

    @Test
    public void toHtml() {
        JTag tag = new JTag("a").setAttrs(new JAttrs().addAttr("attr1", "val1").addAttr("attr2", "val2")).setjBody(new JBody().addElement(
                new JTag("b").setAttrs(new JAttrs().addAttr("attr3", "val3")).setjBody(new JBody().addElement(new JText("text1")).addElement(new JText("text1")))
        ));
        Assert.assertEquals("<a attr2=\"val2\" attr1=\"val1\">\n" +
                "<b attr3=\"val3\">\n" +
                "text1\n" +
                "text1\n" +
                "</b>\n" +
                "</a>", tag.toHtml());
    }

}
