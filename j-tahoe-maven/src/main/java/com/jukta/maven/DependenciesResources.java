package com.jukta.maven;

import com.jukta.jtahoe.resource.*;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @since 1.0
 */
public class DependenciesResources {

    private MavenSession session;

    public DependenciesResources(MavenSession session) {
        this.session = session;
    }

    public List<Resource> getFiles(ResourceType resourceType) {
        List<Resource> res = new ArrayList<>();
        try {
            List<Dependency> dependencies = session.getCurrentProject().getDependencies();
            for (Dependency dependency : dependencies) {
                DefaultArtifact a = new DefaultArtifact(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), dependency.getScope(), dependency.getType(), dependency.getClassifier(), new DefaultArtifactHandler());
                String s = session.getLocalRepository().pathOf(a);
                JarFile jarFile = new JarFile(session.getLocalRepository().getBasedir() + File.separator + s + "." + dependency.getType());
                    JarEntry o =  jarFile.getJarEntry("jtahoe.properties");
                    if (o != null) {
                        Properties p = new Properties();
                        p.load(jarFile.getInputStream(o));
                        String id = p.getProperty("lib.id");
                        o = jarFile.getJarEntry(id + "." + resourceType.getExtension());
                        Resource jTahoeXml = new JarResource(jarFile, o);
                        res.add(jTahoeXml);
                    }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
