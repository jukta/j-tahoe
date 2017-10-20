package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.gen.ArtifactInfo;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * @since 1.0
 */
public class LibraryMetaController {

    private Map<ArtifactInfo, List<ArtifactInfo>> deps = new HashMap<>();


    @PostConstruct
    public void load() throws IOException {

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource[] r = resolver.getResources("classpath*:/jtahoe.properties");
        for (Resource r1 : r) {
            Properties p = new Properties();
            p.load(r1.getInputStream());
            ArtifactInfo info = new ArtifactInfo(p.getProperty("lib.group"), p.getProperty("lib.artifact"), p.getProperty("lib.version"));
            String dep = p.getProperty("dependencies");
            if (dep == null) continue;
            List<ArtifactInfo> l = new ArrayList<>();
            for (String arr : dep.split(";")) {
                String[] a = arr.split(":");
                l.add(new ArtifactInfo(a[0], a[1], a[2]));
            }
            deps.put(info, l);
        }
    }

    public List<ArtifactInfo> getDependencies(ArtifactInfo info) {
        return deps.getOrDefault(info, new ArrayList<>());
    }

}
