package com.jukta.maven;

import com.jukta.jtahoe.gen.xml.XmlBlockModelProvider;
import com.jukta.jtahoe.gen.NodeProcessor;
import com.jukta.jtahoe.resource.ResourceAppender;
import com.jukta.jtahoe.resource.ResourceType;
import com.jukta.jtahoe.resource.Resources;
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

    private void generateJS(File targetDir, Resources resources) throws IOException {
        StringBuilder stringBuilder = ResourceAppender.append(resources, ResourceType.JS);
        File file = new File(targetDir, id + ".js");
        FileWriter w = new FileWriter(file);
        w.append(stringBuilder.toString());
        w.close();
    }

    private void generateCSS(File targetDir, Resources resources) throws IOException {
        StringBuilder stringBuilder = ResourceAppender.append(resources, ResourceType.CSS);
        File file = new File(targetDir, id + ".css");
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
            id = UUID.randomUUID().toString();
            File targetDir = new File(outputDir);
            Resources resources = new FileSystemResources(blocksDir);
            NodeProcessor nodeProcessor = new NodeProcessor();
            List<JavaFileObject> javaFileObjects = nodeProcessor.process(new XmlBlockModelProvider(blocksDir));
            generateSources(targetDir, javaFileObjects);
            mavenProject.addCompileSourceRoot(outputDir);
            generateJS(targetDir, resources);
            generateCSS(targetDir, resources);
            generateProps(targetDir);

            Resource resource = new Resource();
            resource.setDirectory(outputDir);
            resource.addInclude(id + ".js");
            resource.addInclude(id + ".css");
            resource.addInclude("jtahoe.properties");
            mavenProject.addResource(resource);

        } catch (Exception e) {
            throw new MojoFailureException("Source generator error", e);
        }
    }
}
