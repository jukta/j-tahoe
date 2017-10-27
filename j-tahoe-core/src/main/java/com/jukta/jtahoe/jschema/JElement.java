package com.jukta.jtahoe.jschema;

import java.io.IOException;
import java.io.OutputStream;

public interface JElement {

    String toHtml();

    void toHtml(OutputStream outputStream) throws IOException;

}
