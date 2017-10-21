package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.jschema.JAttrs;
import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;
import com.jukta.jtahoe.jschema.JTag;

/**
 * Created by Sergey on 10/19/2017.
 */
public class Script extends Block {

    private boolean autoMode = false;

    @Override
    public void doBody(JBody jBody, Attrs attrs) {
        String artifact = (String) attrs.get("artifact");
        String version = (String) attrs.get("version");
        String resource = (String) attrs.get("resource");
        String auto = (String) attrs.get("auto");

        String src;

        JElement el;

        if (auto.equals("true")) {
            autoMode = true;
            el = new JBody();
        } else {

            if (artifact == null && version == null && resource == null) {
                src = "/app.js";
            } else if (artifact != null && version != null && resource != null) {
                src = "/webjars/" + artifact + "/" + version + "/" + resource;
            } else {
                throw new IllegalArgumentException("'artifact', 'version', 'resource' are required");
            }
            el = getScripEntry(src);
        }

        jBody.addElement(el);

    }

    public boolean isAutoMode() {
        return autoMode;
    }

    public static JElement getScripEntry(String src) {
        JTag element = new JTag("script");
        element.setAttrs(new JAttrs()
                .addAttr("src", src)
                .addAttr("type", "text/javascript")
        );
        element.setjBody(new JBody());
        return element;
    }
}
