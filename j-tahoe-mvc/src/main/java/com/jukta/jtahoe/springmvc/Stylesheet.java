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
public class Stylesheet extends Block {

    private boolean autoMode = false;

    @Override
    public void doBody(JBody jBody, Attrs attrs) {
        String auto = (String) attrs.get("auto");
        String artifact = (String) attrs.get("artifact");
        String version = (String) attrs.get("version");
        String resource = (String) attrs.get("resource");

        String link = (String) attrs.get("href");
        if ("true".equals(auto)) {
            autoMode = true;
            return;
        } else if (link != null) {

        } else if (artifact == null && version == null && resource == null) {
            link = "/app.css";
        } else if (artifact != null && version != null && resource != null) {
            link = "/webjars/" + artifact+ "/" + version + "/" + resource;
        } else {
            throw new IllegalArgumentException("'artifact', 'version', 'resource' are required");
        }

        jBody.addElement(getStylesheetElement(link));
    }

    public boolean isAutoMode() {
        return autoMode;
    }

    public static JElement getStylesheetElement(String link) {
        JTag element = new JTag("link");
        element.setAttrs(new JAttrs()
                .addAttr("rel", "stylesheet")
                .addAttr("href", link)
                .addAttr("type", "text/css")
        );
        element.setjBody(new JBody());
        return element;
    }

}
