package com.jukta.maven;

import com.jukta.jtahoe.resource.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.0
 */
public class FileSystemResources extends CpResourceResolver {

    private String blocksFolder;

    public FileSystemResources(String blocksFolder) {
        this.blocksFolder = blocksFolder;
    }

    @Override
    public List<Resource> getResources(ResourceType resourceType) {
        File root = new File(blocksFolder);
        List<Resource> resources = new ArrayList<>();
        scanDir(root, root, resources, new ResourceExtensionFilter(resourceType));
        return resources;
    }
}
