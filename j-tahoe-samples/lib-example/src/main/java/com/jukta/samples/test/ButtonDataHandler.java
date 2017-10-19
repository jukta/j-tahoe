package com.jukta.samples.test;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.DataHandler;

/**
 * Created by Sergey on 10/19/2017.
 */
public class ButtonDataHandler implements DataHandler {

    @Override
    public Attrs getData(Attrs attrs) {
        attrs.set("message", "This is button from library");
        return attrs;
    }

    @Override
    public boolean async() {
        return false;
    }
}
