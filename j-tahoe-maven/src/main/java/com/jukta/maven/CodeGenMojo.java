package com.jukta.maven;

import com.jukta.jtahoe.gen.xml.XmlBlockModelProvider;
import com.jukta.jtahoe.gen.NodeProcessor;
import com.jukta.jtahoe.resource.*;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.xml.sax.SAXException;

import javax.tools.JavaFileObject;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Sergey Sidorov
 */
@Mojo(name = "code-gen", defaultPhase = LifecyclePhase.PROCESS_SOURCES, threadSafe = true)
public class CodeGenMojo extends AbstractMojo {

    @Parameter( property = "blocksDir", defaultValue = "blocks" )
    private String blocksDir;

    @Parameter( property = "outputDir", defaultValue = "gen" )
    private String outputDir;

    @Component
    protected MavenProject mavenProject;
    @Component
    protected MavenSession mavenSession;
    @Component
    protected BuildPluginManager pluginManager;
//    protected MojoExecutor.ExecutionEnvironment _pluginEnv;

    private String id;

    private void generateSources(File targetDir, List<JavaFileObject> files) throws IOException, SAXException, ParserConfigurationException {
        for (JavaFileObject f : files) {
            System.out.println(f.getName());
            BufferedReader reader = new BufferedReader(f.openReader(false));
            File file = new File(targetDir, f.getName());
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

    private void generateResourceFile(File targetDir, ResourceResolver resources, ResourceType type, boolean includeLibs) throws IOException {
        List<com.jukta.jtahoe.resource.Resource> files = new ArrayList<>();
        if (includeLibs) {
            DependenciesResources lr = new DependenciesResources(mavenSession);
            files.addAll(lr.getFiles(type));
        }



        files.addAll(resources.getResources(new ResourceExtensionFilter(type)));

        StringBuilder stringBuilder = ResourceAppender.append(files);
        File file = new File(targetDir, id + "." + type.getExtension());
        FileWriter w = new FileWriter(file);
        w.append(stringBuilder.toString());
        w.close();
    }

    private void generateProps(File targetDir) throws IOException {
        File file = new File(targetDir, "jtahoe.properties");
        FileWriter w = new FileWriter(file);
        w.append("lib.name=" + mavenProject.getArtifactId() + "\n");
        w.append("lib.version=" + mavenProject.getVersion() + "\n");
        w.append("lib.id=" + id + "\n");
        w.close();
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            boolean jar = "jar".equalsIgnoreCase(mavenProject.getPackaging());
            if (jar) {
                id = UUID.randomUUID().toString();
            } else {
                id = mavenProject.getArtifactId();
            }
            File targetDir = new File(outputDir);
            ResourceResolver resolver = new FileSystemResources(blocksDir);
            NodeProcessor nodeProcessor = new NodeProcessor();
            XmlBlockModelProvider provider = new XmlBlockModelProvider(resolver);
            List<JavaFileObject> javaFileObjects = nodeProcessor.process(provider);
            generateSources(targetDir, javaFileObjects);
            mavenProject.addCompileSourceRoot(outputDir);
            generateResourceFile(targetDir, resolver, ResourceType.JS, !jar);
            generateResourceFile(targetDir, resolver, ResourceType.CSS, !jar);

            Resource resource = new Resource();
            resource.setDirectory(outputDir);
            resource.addInclude(id + ".js");
            resource.addInclude(id + ".css");
            if (jar) {
                generateProps(targetDir);
                resource.addInclude("jtahoe.properties");
            }
            mavenProject.addResource(resource);
        } catch (Exception e) {
            throw new MojoFailureException("Source generator error", e);
        }
    }
}
