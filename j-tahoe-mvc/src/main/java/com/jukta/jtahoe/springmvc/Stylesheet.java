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

    @Override
    public JElement body(Attrs attrs) {

        String artifact = (String) attrs.get("artifact");
        String version = (String) attrs.get("version");
        String resource = (String) attrs.get("resource");

        String src;

        if (artifact == null && version == null && resource == null) {
            src = "/app.css";
        } else if (artifact != null && version != null && resource != null) {
            src = "/webjars/" + artifact+ "/" + version + "/" + resource;
        } else {
            throw new IllegalArgumentException("'artifact', 'version', 'resource' are required");
        }


        JTag element = new JTag("link");
        element.setAttrs(new JAttrs()
                .addAttr("rel", "stylesheet")
                .addAttr("href", src)
                .addAttr("type", "text/css")
        );
        element.setjBody(new JBody());
        return element;
    }
}