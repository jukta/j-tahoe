package com.jukta.jtahoe.loader;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.List;

/**
 * Created by Dmitriy Dobrovolskiy on 29.03.2016.
 *
 * @since *.*.*
 */
public class MemoryClassLoader extends ClassLoader {
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final MemoryFileManager manager = new MemoryFileManager(this.compiler);

    public MemoryClassLoader(List<JavaFileObject> files) {
        this.compiler.getTask(null, this.manager, null, null, null, files).call();
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
}