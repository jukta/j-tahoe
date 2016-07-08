package com.jukta.jtahoe.resource;

import com.jukta.jtahoe.file.JTahoeXml;
import sun.misc.IOUtils;

import java.io.IOException;
import java.util.List;

/**
 * @since 1.0
 */
public class ResourceAppender {

    public static StringBuilder append(Resources resources, ResourceType resourceType) throws IOException {
        List<JTahoeXml> files = resources.getFiles(resourceType);
        StringBuilder sb = new StringBuilder();
        for (JTahoeXml f : files) {
            String cont = new String(IOUtils.readFully(f.getInputSource().getByteStream(), -1, false));
            sb.append(cont);
        }
        return sb;
    }

}
