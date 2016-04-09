package com.jukta.jtahoe.loader;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
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
            CompiledJavaObject compiledObject = this.manager.map.remove(name);
            if (compiledObject != null) {
                byte[] array = compiledObject.toByteArray();
                return defineClass(name, array, 0, array.length);
            }
        }
        return super.findClass(name);
    }

    private List<String> getClasspathOptions() {
        List<String> options = new ArrayList<String>();
        options.add("-classpath");
        StringBuilder sb = new StringBuilder();
        URLClassLoader urlClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        for (URL url : urlClassLoader.getURLs())
            sb.append(url.getFile()).append(File.pathSeparator);
        options.add(sb.toString());
        return options;
    }
}