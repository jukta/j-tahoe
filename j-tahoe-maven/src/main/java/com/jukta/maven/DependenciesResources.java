package com.jukta.maven;

import com.jukta.jtahoe.gen.file.JTahoeResourceXml;
import com.jukta.jtahoe.gen.file.JTahoeXml;
import com.jukta.jtahoe.resource.ResourceType;
import com.jukta.jtahoe.resource.Resources;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @since 1.0
 */
public class DependenciesResources extends Resources {

    private MavenSession session;

    public DependenciesResources(MavenSession session) {
        super("");
        this.session = session;
    }

    public List<JTahoeXml> getFiles(ResourceType resourceType) {
        List<JTahoeXml> res = new ArrayList<>();
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
                        InputStream inputStream = jarFile.getInputStream(o);
                        JTahoeXml jTahoeXml = new JTahoeResourceXml(inputStream, o.getName());
                        res.add(jTahoeXml);
                    }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
