package com.jukta.jtahoe.servlet;

import com.jukta.jtahoe.resource.ResourceType;

/**
 * @since 1.0
 */
public class JsFilter extends CssFilter {

    protected ResourceType getFilter() {
        return ResourceType.CSS;
    }

}
