package com.jukta.maven;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.NodeProcessor;
import com.jukta.jtahoe.gen.xml.XthBlockModelProvider;
import com.jukta.jtahoe.resource.ResourceAppender;
import com.jukta.jtahoe.resource.ResourceResolver;
import com.jukta.jtahoe.resource.ResourceType;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

import javax.tools.JavaFileObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
    private Log log;
    private File compileDir;

    private String jsDependecies;
    private String cssDependecies;

    public JarBuilder(String blocksDir, String resourceSrcDir, File targetDir, MavenProject mavenProject, MavenSession mavenSession, Log log) {
        this.blocksDir = blocksDir;
        this.resourceSrcDir = resourceSrcDir;
        this.targetDir = targetDir;
        this.mavenProject = mavenProject;
        this.mavenSession = mavenSession;
        id = UUID.randomUUID().toString();
        this.log = log;
        compileDir = new File(targetDir, "java");
    }

    public void generateSources(ResourceResolver resolver) throws IOException {
        XthBlockModelProvider provider = new XthBlockModelProvider(resolver);
        NodeProcessor nodeProcessor = new NodeProcessor();
        nodeProcessor.setBuildId(id);
//        Map<String, List<JavaFileObject>> javaFileObjects = ;


        log.info("Generating source files");
        for (GenContext.Package aPackage : nodeProcessor.process(provider).values()) {
            for (JavaFileObject f : aPackage.getJavaFileObjects()) {
                log.info(f.getName());
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
        }

        mavenProject.addCompileSourceRoot(compileDir.getAbsolutePath());
    }

    private void generateResourceFile(ResourceResolver resources, ResourceType type) throws IOException {

        List<com.jukta.jtahoe.resource.Resource> files = new ArrayList<>();
        files.addAll(resources.getResources(type));

        StringBuilder stringBuilder = ResourceAppender.append(files);
        File file = new File(resourceDestDir, mavenProject.getArtifactId() + "." + type.getExtension());
        log.info("Generated resource file: " + file.getName());
        FileWriter w = new FileWriter(file);
        w.append(stringBuilder.toString());
        w.close();
    }

    private void copyResources() throws IOException {
        File resDir = resourceDestDir;
//        resDir.mkdirs();
//        log.info("Creating resource dir: " + resDir.getName());
        FileUtils.copyDirectoryStructure(new File(resourceSrcDir), resDir);
    }

    protected void generateProps() throws IOException {
        File file = new File(resourceDestDir, "jtahoe.properties");
        FileWriter w = new FileWriter(file);
        w.append("lib.id=" + id + "\n");
        w.append("lib.group=" + mavenProject.getGroupId() + "\n");
        w.append("lib.artifact=" + mavenProject.getArtifactId() + "\n");
        w.append("lib.version=" + mavenProject.getVersion() + "\n");


        if (jsDependecies != null) {
            w.append("dependencies.js=");
            for (String res : jsDependecies.split(";")) {
                String dep = getDepEntry(res);
                if (dep != null) w.append(dep + ";");
            }
            w.append("\n");
        }

        if (cssDependecies != null) {
            w.append("dependencies.css=");
            for (String res : cssDependecies.split(";")) {
                String dep = getDepEntry(res);
                if (dep != null) w.append(dep + ";");
            }
            w.append("\n");
        }

        w.close();

        Resource resource = new Resource();
        resource.setDirectory(resourceDestDir.getAbsolutePath());
        resource.setIncludes(Collections.singletonList("jtahoe.properties"));
        mavenProject.addResource(resource);
    }

    private String getDepEntry(String file) throws IOException {


        if (new File(resourceDestDir, file).exists()) {
            return "/webjars/" + mavenProject.getArtifactId() + "/" + mavenProject.getVersion() + "/" + file;
        }

        for (Artifact d : mavenProject.getArtifacts()) {
            Artifact a = mavenSession.getLocalRepository().find(d);
            JarEntry entry = new JarFile(a.getFile().getAbsolutePath()).getJarEntry("META-INF/resources/webjars/" + a.getArtifactId() + "/" + a.getVersion() + "/" + file);
            if (entry == null) continue;

            return "/webjars/" + a.getArtifactId() + "/" + a.getVersion() + "/" + file;
        }
        return null;
    }

    public void generate() throws IOException {
        resourceDestDir = new File(targetDir, "resources");
        resourceDestDir.mkdirs();

        Resource resource = new Resource();
        resource.setDirectory(resourceDestDir.getAbsolutePath());
        resource.setExcludes(Collections.singletonList("jtahoe.properties"));
        mavenProject.addResource(resource);

        ResourceResolver resolver = new FileSystemResources(blocksDir);

        generateSources(resolver);
        generateResourceFile(resolver, ResourceType.CSS);
        generateResourceFile(resolver, ResourceType.JS);

        for (Resource r : mavenProject.getResources()) {
            r.setTargetPath(mavenProject.getBuild().getOutputDirectory() + "/META-INF/resources/webjars/" + mavenProject.getArtifactId() + "/" + mavenProject.getVersion());
        }

//        copyResources();
        generateProps();
    }

    public void setJsDependecies(String jsDependecies) {
        this.jsDependecies = jsDependecies;
    }

    public void setCssDependecies(String cssDependecies) {
        this.cssDependecies = cssDependecies;
    }
}
