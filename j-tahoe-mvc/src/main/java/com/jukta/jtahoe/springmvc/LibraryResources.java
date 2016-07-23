package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.gen.file.JTahoeResourceXml;
import com.jukta.jtahoe.gen.file.JTahoeXml;
import com.jukta.jtahoe.resource.ResourceType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @since 1.0
 */
public class LibraryResources {
    private List<String> libs = new ArrayList<>();
    private PathMatchingResourcePatternResolver resolver;

    public LibraryResources() {
         resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] r = resolver.getResources("classpath*:/**/jtahoe.properties");
            for (Resource r1 : r) {
                Properties p = new Properties();
                p.load(r1.getInputStream());
                String libId = p.getProperty("lib.id");
                libs.add(libId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<JTahoeXml> getFiles(ResourceType resourceType) {
        List<JTahoeXml> res = new ArrayList<>();
        try {
            for (String id : libs) {
                Resource[] r = resolver.getResources("classpath*:/**/" + id + "." + resourceType.getExtension());
                JTahoeXml jTahoeXml = new JTahoeResourceXml(r[0].getInputStream(), r[0].getURI().toString());
                res.add(jTahoeXml);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
