package com.jukta.jtahoe.loader;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.net.URI;

/**
 * Created by Dmitriy Dobrovolskiy on 29.03.2016.
 *
 * @since *.*.*
 */
public class CompiledJavaObject extends SimpleJavaFileObject {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public CompiledJavaObject(String name, Kind kind) {
        super(URI.create("file:///" + name.replace('.', '/') + kind.extension), kind);
    }

    public byte[] toByteArray() {
        return this.outputStream.toByteArray();
    }

    @Override
    public ByteArrayOutputStream openOutputStream() {
        return this.outputStream;
    }
}
