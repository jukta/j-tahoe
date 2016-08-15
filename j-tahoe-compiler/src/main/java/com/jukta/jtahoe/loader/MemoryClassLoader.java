package com.jukta.jtahoe.loader;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Dmitriy Dobrovolskiy on 29.03.2016.
 *
 * @since *.*.*
 */
public class MemoryClassLoader extends ClassLoader {
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final MemoryFileManager manager = new MemoryFileManager(this.compiler);

    public MemoryClassLoader(List<JavaFileObject> files) throws Exception {
        super(Thread.currentThread().getContextClassLoader());
        this.compiler.getTask(null, this.manager, null, getClasspathOptions(), null, files).call();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        synchronized (this.manager) {
            CompiledJavaObject compiledObject = MemoryFileManager.map.get(name);
            if (compiledObject != null) {
                byte[] array = compiledObject.toByteArray();
                return defineClass(name, array, 0, array.length);
            }
        }
        return super.findClass(name);
    }

    @Override
    public URL getResource(String name) {
        try {
            if (super.getResource(name) != null) {
                return super.getResource(name);
            } else {
                CompiledJavaObject object = MemoryFileManager.map.get(name.substring(0, name.indexOf(".")).replace("/", "."));
                return object != null ? object.toUri().toURL() : null;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (super.getResourceAsStream(name) != null) {
            return super.getResourceAsStream(name);
        } else {
            CompiledJavaObject object = MemoryFileManager.map.get(name.substring(0, name.indexOf(".")).replace("/", "."));
            return object != null ? new ByteArrayInputStream(object.openOutputStream().toByteArray()) : null;
        }
    }

    private List<String> getClasspathOptions() {
        List<String> options = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader instanceof URLClassLoader) {
            options.add("-classpath");
            StringBuilder sb = new StringBuilder();
            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
            for (URL url : urlClassLoader.getURLs())
                sb.append(url.getFile()).append(File.pathSeparator);
            options.add(sb.toString());
        } else if (classLoader != null && "org.jboss.modules.ModuleClassLoader".equals(classLoader.getClass().getName())) {
            try {
                options.add("-classpath");
                StringBuilder sb = new StringBuilder();
                Enumeration<URL> resources = classLoader.getResources("");
                while (resources.hasMoreElements()) {
                    URL url = resources.nextElement();
                    Object virtualFile = url.getContent();
                    if (("org.jboss.vfs.VirtualFile").equals(virtualFile.getClass().getName())) {
                        Method getPhysicalFile = virtualFile.getClass().getMethod("getPhysicalFile");
                        File vfile = (File) getPhysicalFile.invoke(virtualFile);
                        for (File file : vfile.getParentFile().listFiles()) {
                            sb.append(file).append(File.pathSeparator);
                        }
                    }
                }
                options.add(sb.toString());
            } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return options;
    }
}