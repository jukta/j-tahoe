package com.jukta.jtahoe;

import com.jukta.jtahoe.gen.NodeProcessor;
import groovy.lang.GroovyClassLoader;

import javax.tools.JavaFileObject;
import java.util.List;

/**
 * @since 1.0
 */
public class RuntimeBlockFactory extends BlockFactory {

    public RuntimeBlockFactory(BlockModelProvider blockModelProvider) {
        try {
            GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
            List<JavaFileObject> javaFileObjects = new NodeProcessor().process(blockModelProvider);
            for (JavaFileObject fileObject : javaFileObjects) {
                String code = String.valueOf(fileObject.getCharContent(false));
                groovyClassLoader.parseClass(code);
            }
            classLoader = groovyClassLoader;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
