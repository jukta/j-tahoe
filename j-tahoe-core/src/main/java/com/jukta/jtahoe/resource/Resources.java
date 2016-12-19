package com.jukta.jtahoe.resource;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Sidorov
 */
public class Resources extends CpResourceResolver {

    @Override
    public List<Resource> getResources(ResourceFilter resourceFilter) {
//        URL url = getClass().getClassLoader().getResource("");
//        File root;
//        if ("vfs".equals(url.getProtocol())) {
//            root = VfsUtils.getFile(url);
//        } else {
//            root = new File(url.getFile()).getAbsoluteFile();
//        }
//        List<Resource> resources = new ArrayList<>();
//        scanDir(root, root, resources, resourceFilter);
//        return resources;
        return super.getResources(resourceFilter);
    }

}
