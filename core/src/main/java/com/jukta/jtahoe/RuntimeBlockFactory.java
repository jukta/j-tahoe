package com.jukta.jtahoe;

import com.jukta.jtahoe.gen.NodeProcessor;
import com.jukta.jtahoe.loader.MemoryClassLoader;

import javax.tools.JavaFileObject;
import java.util.List;

/**
 * @since 1.0
 */
public class RuntimeBlockFactory extends BlockFactory {

    public RuntimeBlockFactory(BlockModelProvider blockModelProvider) {
        try {
            List<JavaFileObject> javaFileObjects = new NodeProcessor().process(blockModelProvider);
            classLoader = new MemoryClassLoader(javaFileObjects);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
