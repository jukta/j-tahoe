package com.jukta.jtahoe.gen.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.MatchResult;

public class BasicParser {

    private Handler handler;
    private boolean bypass = false;

    public BasicParser(Handler handler) {
        this.handler = handler;
    }

    public void parse(InputStream is) {
        SequenceInputStream sis = new SequenceInputStream(new ByteArrayInputStream(" ".getBytes()), is);
        Scanner sc = new Scanner(sis);
        String[] d = new String[] {"<", ">"};
        int i = 0;
        sc.useDelimiter(d[i % 2]);
        while (sc.hasNext()) {
            String l = sc.next();
            if (i % 2 != 0) {
                l = l + d[i % 2];
            } else {
                l = l.substring(1);
            }
            if (handle(l)) {
                i++;
                sc.useDelimiter(d[i % 2]);
            }
        }
        sc.close();
    }

    boolean cdata = false;
    boolean cmt = false;

    protected boolean handle(String l) {
        boolean res = true;
        if (!bypass && l.startsWith("<![CDATA[")) {
            if (!l.trim().endsWith("]]>")) {
                bypass = true;
                cdata = true;
            }
            handler.text(l);
        } else if (cdata && l.trim().endsWith("]]>")) {
            bypass = false;
            cdata = false;
            handler.text(l);
        } else if (!bypass && l.startsWith("<!--")) {
            if (!l.trim().endsWith("-->")) {
                bypass = true;
                cmt = true;
            }
            handler.text(l);
        } else if (cmt && l.trim().endsWith("-->")) {
            bypass = false;
            cmt = false;
            handler.text(l);
        } else if (bypass) {
            handler.text(l);
        } else if (l.startsWith("</")) {
            end(l, handler);
        } else if (l.startsWith("<")) {
            start(l, handler);
        } else {
            handler.text(l);
        }
        if (cdata || cmt) res = false;
        return res;
    }

    protected void start(String line, Handler handler) {
        line = line.replaceAll("\n", "").replaceAll("\r", "");
        Scanner s = new Scanner(line);
        s.findInLine("<((\\w+):)?([a-zA-Z_0-9-]+)");
        MatchResult n = s.match();

        String attrPat = "([a-zA-Z:-]+)\\s*(=\\s*\"([^\"]+)\")?";
        Map<String, String> attrs = new HashMap<>();
        while (s.findInLine(attrPat) != null) {
            MatchResult result = s.match();
            attrs.put(result.group(1), result.group(3));
        }
        handler.start(n.group(2), n.group(3), attrs, line.endsWith("/>"));
    }

    protected void end(String line, Handler handler) {
        Scanner s = new Scanner(line);
        s.findInLine("</((\\w+):)?(\\w+)");
        MatchResult n = s.match();
        handler.end(n.group(2), n.group(3));
    }

    public Handler getHandler() {
        return handler;
    }

    public boolean isBypass() {
        return bypass;
    }

    public void setBypass(boolean bypass) {
        this.bypass = bypass;
    }

    public interface Handler {

        void start(String ns, String name, Map<String, String> attrs, boolean selfClosing);
        void text(String text);
        void end(String ns, String name);

    }

}
