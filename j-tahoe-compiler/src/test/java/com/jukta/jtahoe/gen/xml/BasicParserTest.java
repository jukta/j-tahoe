package com.jukta.jtahoe.gen.xml;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * @since 1.0
 */
public class BasicParserTest {

    @Test
    public void simpleHtmlBlock() {

        StringBuffer sb = new StringBuffer();

        BasicParser p = new BasicParser(new BasicParser.Handler() {
            @Override
            public void start(String ns, String name, Map<String, String> attrs, boolean selfClosing) {
                sb.append(ns + ":" + name + attrs + "/");
            }

            @Override
            public void text(String text) {
                sb.append(text);
            }

            @Override
            public void end(String ns, String name) {
                sb.append("/" + ns + ":" + name);
            }
        });

        p.parse(new ByteArrayInputStream(("<div>" +
                "<!--\n <br/> \n -->" +
                "</div>").getBytes()));

        System.out.println(sb.toString());

    }

}
