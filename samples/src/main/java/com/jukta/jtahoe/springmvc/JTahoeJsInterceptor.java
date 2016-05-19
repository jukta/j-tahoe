package com.jukta.jtahoe.springmvc;

import java.io.FilenameFilter;

/**
 * @author Sergey Sidorov
 */
public class JTahoeJsInterceptor extends JTahoeCssInterceptor {

    @Override
    protected FilenameFilter getFilter() {
        return new Resources.ExtensionFilter("js");
    }
}
