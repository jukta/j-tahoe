package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Resources;

import java.io.FilenameFilter;

/**
 * @author Sergey Sidorov
 */
public class JsInterceptor extends CssInterceptor {

    private String contentType = "text/javascript";

    @Override
    protected FilenameFilter getFilter() {
        return new Resources.ExtensionFilter("js");
    }

    public String getContentType() {
        return contentType;
    }
}
