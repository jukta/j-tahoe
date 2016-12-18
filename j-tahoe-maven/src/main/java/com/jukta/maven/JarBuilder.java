package com.jukta.maven;

import com.jukta.jtahoe.gen.NodeProcessor;
import com.jukta.jtahoe.gen.xml.XmlBlockModelProvider;
import com.jukta.jtahoe.resource.ResourceAppender;
import com.jukta.jtahoe.resource.ResourceExtensionFilter;
import com.jukta.jtahoe.resource.ResourceResolver;
import com.jukta.jtahoe.resource.ResourceType;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

import javax.tools.JavaFileObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.maven.model.Resource;
import org.codehaus.plexus.util.FileUtils;

/**
 * @since 1.0
 */
public class JarBuilder {

    private String blocksDir;
    private String resourceSrcDir;
    private File resourceDestDir;
    private File targetDir;
    private MavenProject mavenProject;
    private MavenSession mavenSession;
    protected String id;

    public JarBuilder(String blocksDir, String resourceSrcDir, File targetDir, MavenProject mavenProject, MavenSession mavenSession) {
        this.blocksDir = blocksDir;
        this.resourceSrcDir = resourceSrcDir;
        this.targetDir = targetDir;
        this.mavenProject = mavenProject;
        this.mavenSession = mavenSession;
        id = UUID.randomUUID().toString();
    }

    private void generateSources(ResourceResolver resolver) throws IOException {
        XmlBlockModelProvider provider = new XmlBlockModelProvider(resolver);
        NodeProcessor nodeProcessor = new NodeProcessor();
        List<JavaFileObject> javaFileObjects = nodeProcessor.process(provider);

        File compileDir = new File(targetDir, "java");

        for (JavaFileObject f : javaFileObjects) {
            System.out.println(f.getName());
            BufferedReader reader = new BufferedReader(f.openReader(false));
            File file = new File(compileDir, f.getName());
            file.getParentFile().mkdirs();
            FileWriter w = new FileWriter(file);
            String line;
            while ((line = reader.readLine()) != null) {
                w.append(line);
            }
            w.close();
            reader.close();
        }

        mavenProject.addCompileSourceRoot(compileDir.getAbsolutePath());
    }

    private void generateResourceFile(ResourceResolver resources, ResourceType type) throws IOException {

        List<com.jukta.jtahoe.resource.Resource> files = new ArrayList<>();
        files.addAll(resources.getResources(new ResourceExtensionFilter(type)));

        StringBuilder stringBuilder = ResourceAppender.append(files);
        File file = new File(resourceDestDir, id + "." + type.getExtension());
        FileWriter w = new FileWriter(file);
        w.append(stringBuilder.toString());
        w.close();
    }

    private void copyResources() throws IOException {
        File resDir = new File(resourceDestDir, id);
        resDir.mkdirs();
        FileUtils.copyDirectory(new File(resourceSrcDir), resDir);
    }

    protected void generateProps() throws IOException {
        File file = new File(resourceDestDir, "jtahoe.properties");
        FileWriter w = new FileWriter(file);
        w.append("lib.name=" + mavenProject.getArtifactId() + "\n");
        w.append("lib.version=" + mavenProject.getVersion() + "\n");
        w.append("lib.id=" + id + "\n");
        w.close();
    }

    public void generate() throws IOException {
        resourceDestDir = new File(targetDir, "resources");
        resourceDestDir.mkdirs();
        Resource resource = new Resource();
        resource.setDirectory(resourceDestDir.getAbsolutePath());
        mavenProject.addResource(resource);

        ResourceResolver resolver = new FileSystemResources(blocksDir);

        generateSources(resolver);
        generateResourceFile(resolver, ResourceType.CSS);
        generateResourceFile(resolver, ResourceType.JS);
        copyResources();
        generateProps();
    }

}
