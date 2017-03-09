package com.jukta.jtahoe.jschema;

import org.junit.Assert;
import org.junit.Test;

/**
 * @since 1.0
 */
public class JTagTest {

    @Test
    public void toJson() {
        JTag tag = new JTag("a").setAttrs(new JAttrs().addAttr("attr1", "val1").addAttr("attr2", "val2")).setjBody(new JBody()
                .addElement(new JTag("b").setAttrs(new JAttrs().addAttr("attr3", "val3")).setjBody(new JBody().addElement(new JText("text1")).addElement(new JText("text1"))))
                .addElement(new JTag("b1").setAttrs(new JAttrs().addAttr("attr4", "val4")).setjBody(new JBody().addElement(new JText("text2")).addElement(new JText("text2"))))
                .addElement(new JBody())
        );
        Assert.assertEquals("{\"_name\":\"a\",\"_attrs\": {\"attr2\":\"val2\",\"attr1\":\"val1\"},\"_\": [{\"_name\":\"b\",\"_attrs\": {\"attr3\":\"val3\"},\"_\": [\"text1\",\"text1\"]},{\"_name\":\"b1\",\"_attrs\": {\"attr4\":\"val4\"},\"_\": [\"text2\",\"text2\"]}]}", tag.toJson());
    }

    @Test
    public void toJson1() {

        JBody b1 = new JBody();
        JBody b2 = new JBody();
        b2.addElement(new JTag("div"));
        JBody b3 = new JBody();
        b3.addElement(new JTag("div"));

        b1.addElement(b2);
        b1.addElement(b3);
//        System.out.println(b1.toJson());

        Assert.assertEquals("\"_\": [{\"_name\":\"div\"},{\"_name\":\"div\"}]", b1.toJson());
    }

    @Test
    public void toJson2() {

        JBody b1 = new JBody();
        JTag b2 = new JTag("div").setAttrs(new JAttrs().addAttr("class", "c1"));
        JTag b3 = new JTag("div").setAttrs(new JAttrs().addAttr("class", "c2")).setjBody(new JBody());

        b1.addElement(b2);
        b1.addElement(b3);
//        System.out.println(b1.toJson());

        Assert.assertEquals("\"_\": [{\"_name\":\"div\",\"_attrs\": {\"class\":\"c1\"}},{\"_name\":\"div\",\"_attrs\": {\"class\":\"c2\"}}]", b1.toJson());
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
