package com.jukta.jtahoe.resource;

import sun.misc.IOUtils;

import java.io.IOException;
import java.util.List;

/**
 * @since 1.0
 */
public class ResourceAppender {

    public static StringBuilder append(List<Resource> files) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Resource f : files) {
            String cont = new String(IOUtils.readFully(f.getInputStream(), -1, false));
            sb.append(cont);
        }
        return sb;
    }

}
