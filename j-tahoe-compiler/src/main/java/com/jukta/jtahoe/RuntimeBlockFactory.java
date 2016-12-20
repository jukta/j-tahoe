package com.jukta.jtahoe;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.NodeProcessor;
import groovy.lang.GroovyClassLoader;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @since 1.0
 */
public class RuntimeBlockFactory extends BlockFactory {

    public RuntimeBlockFactory(BlockModelProvider blockModelProvider) {
        try {
            GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
            Map<String, GenContext.Package> map = new NodeProcessor().process(blockModelProvider);
            List<GenContext.Package> compiled = new ArrayList<>();
            for (GenContext.Package aPackage : map.values()){
                compilePackage(aPackage, groovyClassLoader, map, compiled);
            }
            classLoader = groovyClassLoader;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void compilePackage(GenContext.Package aPackage, GroovyClassLoader groovyClassLoader, Map<String, GenContext.Package> map, List<GenContext.Package> compiled) throws IOException {
        if (aPackage == null || compiled.contains(aPackage)) return;

        for (String pack : aPackage.getDependedPackages()) {
            compilePackage(map.get(pack), groovyClassLoader, map, compiled);
        }

        for (JavaFileObject fileObject : aPackage.getJavaFileObjects()) {
            String code = String.valueOf(fileObject.getCharContent(false));
            groovyClassLoader.parseClass(code);
        }
        compiled.add(aPackage);
    }
}
