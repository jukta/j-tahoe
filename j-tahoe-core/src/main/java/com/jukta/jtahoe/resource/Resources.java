package com.jukta.jtahoe.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Sidorov
 */
public class Resources extends CpResourceResolver {

    private String blocksFolder;

    public Resources(String blocksFolder) {
        this.blocksFolder = blocksFolder;
    }

    @Override
    public List<Resource> getResources(ResourceFilter resourceFilter) {
        File root = new File(getClass().getClassLoader().getResource(blocksFolder).getFile());
        List<Resource> resources = new ArrayList<>();
        scanDir(root, resources, resourceFilter);
        return resources;
    }

}
