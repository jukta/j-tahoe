package com.jukta.jtahoe.loader;

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitriy Dobrovolskiy on 29.03.2016.
 *
 * @since *.*.*
 */
public class MemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    public final Map<String, CompiledJavaObject> map = new HashMap<String, CompiledJavaObject>();

    public MemoryFileManager(JavaCompiler compiler) {
        super(compiler.getStandardFileManager(null, null, null));
    }

    @Override
    public CompiledJavaObject getJavaFileForOutput(Location location, String name, Kind kind, FileObject source) {
        CompiledJavaObject mc = new CompiledJavaObject(name, kind);
        this.map.put(name, mc);
        return mc;
    }
}