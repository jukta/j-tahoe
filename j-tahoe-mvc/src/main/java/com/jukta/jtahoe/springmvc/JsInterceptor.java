package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.resource.ResourceType;
import com.jukta.jtahoe.resource.Resources;

import java.io.FilenameFilter;

/**
 * @author Sergey Sidorov
 */
public class JsInterceptor extends CssInterceptor {

    private String contentType = "text/javascript";

    @Override
    protected ResourceType getFilter() {
        return ResourceType.JS;
    }

    public String getContentType() {
        return contentType;
    }
}
