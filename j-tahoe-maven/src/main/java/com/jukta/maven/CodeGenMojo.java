package com.jukta.maven;

import com.jukta.jtahoe.DirHandler;
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

import java.io.File;

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

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            DirHandler dirHandler = new DirHandler(new File(blocksDir));
            dirHandler.generateSources(new File(outputDir));
            mavenProject.addCompileSourceRoot(outputDir);
        } catch (Exception e) {
            throw new MojoFailureException("Source generator error", e);
        }
    }
}
