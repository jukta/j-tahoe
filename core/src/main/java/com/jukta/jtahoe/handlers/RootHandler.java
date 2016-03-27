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
//        super.start();
    }

    @Override
    public void text(String text) {
//        super.text(text);
    }

    @Override
    public void end() {
//        super.end();
    }


}
