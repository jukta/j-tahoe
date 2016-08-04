package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.resource.ResourceType;

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
