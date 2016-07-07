package com.jukta.maven;

import com.jukta.jtahoe.gen.DirHandler;
import com.jukta.jtahoe.resource.ResourceType;
import com.jukta.jtahoe.resource.Resources;
import com.jukta.jtahoe.file.JTahoeXml;
import org.apache.maven.execution.MavenSession;
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


    public void generateSources(File targetDir, List<JavaFileObject> files) throws IOException, SAXException, ParserConfigurationException {
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

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            Resources resources = new Resources(blocksDir);
            List<JTahoeXml> list =  resources.getFiles(ResourceType.XML);
            DirHandler dirHandler = new DirHandler(new File(blocksDir));
            List<JavaFileObject> javaFileObjects = dirHandler.getJavaFiles(list);
            generateSources(new File(outputDir), javaFileObjects);
            mavenProject.addCompileSourceRoot(outputDir);
        } catch (Exception e) {
            throw new MojoFailureException("Source generator error", e);
        }
    }
}
