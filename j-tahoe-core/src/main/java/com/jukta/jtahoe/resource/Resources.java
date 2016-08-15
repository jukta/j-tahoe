package com.jukta.jtahoe.resource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
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
        URL url = getClass().getClassLoader().getResource(blocksFolder);
        File root = null;
        if ("vfs".equals(url.getProtocol())) {
            try {
                Object virtualFile = url.getContent();
                if (("org.jboss.vfs.VirtualFile").equals(virtualFile.getClass().getName())) {
                    Method getPhysicalFile = virtualFile.getClass().getMethod("getPhysicalFile");
                    root = (File) getPhysicalFile.invoke(virtualFile);
                }
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            root = new File(url.getFile());
        }
        List<Resource> resources = new ArrayList<>();
        scanDir(root, resources, resourceFilter);
        return resources;
    }

}
