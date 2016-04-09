package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.GenContext;

import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public class RootHandler extends AbstractHandler {

    public RootHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
    }

    @Override
    public void start() {
        String pref = getAttrs().get("pref");
        if (pref == null) {
            pref = "lc";
        }
        getGenContext().getPrefixes().put(pref, getCurPackage());
    }

    @Override
    public void end() {
//        super.end();
    }


}
