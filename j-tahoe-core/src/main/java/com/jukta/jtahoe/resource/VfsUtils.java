package com.jukta.jtahoe.resource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * @since 1.0.1
 */
public class VfsUtils {
    public static File getFile(URL vfsFileUrl) {
        try {
            Object virtualFile = vfsFileUrl.getContent();
            if (("org.jboss.vfs.VirtualFile").equals(virtualFile.getClass().getName())) {
                Method getPhysicalFile = virtualFile.getClass().getMethod("getPhysicalFile");
                return (File) getPhysicalFile.invoke(virtualFile);
            }
            return new File(vfsFileUrl.getFile());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
