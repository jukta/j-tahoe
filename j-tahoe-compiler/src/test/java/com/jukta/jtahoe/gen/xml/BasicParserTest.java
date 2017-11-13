package com.jukta.jtahoe.gen.xml;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Map;
import static org.junit.Assert.assertEquals;

/**
 * @since 1.0
 */
public class BasicParserTest {

    @Test
    public void comments() {

        StringBuffer sb = new StringBuffer();

        BasicParser p = new BasicParser(new BasicParser.Handler() {
            @Override
            public void start(String ns, String name, Map<String, String> attrs, boolean selfClosing) {
                sb.append(ns).append(":").append(name).append(attrs).append(selfClosing ? "/" : "");
            }

            @Override
            public void text(String text) {
                sb.append(text);
            }

            @Override
            public void end(String ns, String name) {
                sb.append("/").append(ns).append(":").append(name);
            }
        });

        p.parse(new ByteArrayInputStream(("<div>" +
                "<!--<br/>-->" +
                "</div>").getBytes()));

        assertEquals("null:div{}<!--<br/>-->/null:div", sb.toString());

        sb.delete(0, sb.length());
        p.parse(new ByteArrayInputStream(("<div>" +
                "<!--\n<br/>\n-->\n    " +
                "</div>").getBytes()));

        assertEquals("null:div{}<!--\n<br/>\n-->\n" +
                "    /null:div", sb.toString());


    }

    @Test
    public void comments1() {

        StringBuffer sb = new StringBuffer();

        BasicParser p = new BasicParser(new BasicParser.Handler() {
            @Override
            public void start(String ns, String name, Map<String, String> attrs, boolean selfClosing) {
                sb.append(ns).append(":").append(name).append(attrs).append(selfClosing ? "/" : "");
            }

            @Override
            public void text(String text) {
                sb.append(text);
            }

            @Override
            public void end(String ns, String name) {
                sb.append("/").append(ns).append(":").append(name);
            }
        });

        p.parse(new ByteArrayInputStream(("<!--<th:parent/>-->B<div></div>").getBytes()));

        assertEquals("<!--<th:parent/>-->Bnull:div{}/null:div", sb.toString());

    }

}
