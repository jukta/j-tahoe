package com.jukta.maven;

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

import java.io.*;
import java.util.Iterator;

/**
 * @author Sergey Sidorov
 */
@Mojo(name = "code-gen", defaultPhase = LifecyclePhase.PROCESS_SOURCES, threadSafe = true)
public class CodeGenMojo extends AbstractMojo {

    @Parameter( property = "blocksDir", defaultValue = "blocks" )
    private String blocksDir;

    @Parameter( property = "outputDir", defaultValue = "gen" )
    private String outputDir;

    @Parameter( property = "resourcesDir", defaultValue = "public" )
    private String resourcesDir;

    @Component
    protected MavenProject mavenProject;
    @Component
    protected MavenSession mavenSession;
    @Component
    protected BuildPluginManager pluginManager;
//    protected MojoExecutor.ExecutionEnvironment _pluginEnv;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            removeFromResources(blocksDir);
            removeFromResources(resourcesDir);
            File targetDir = new File(outputDir);
            boolean jar = "jar".equalsIgnoreCase(mavenProject.getPackaging());
            if (jar) {
                JarBuilder builder = new JarBuilder(blocksDir, resourcesDir, targetDir, mavenProject, mavenSession, getLog());
                builder.generate();
            } else {
                WarBuilder builder = new WarBuilder(blocksDir, resourcesDir, targetDir, mavenProject, mavenSession, getLog());
                builder.generate();
            }
        } catch (Exception e) {
            throw new MojoFailureException("Source generator error", e);
        }
    }

    private void removeFromResources(String dir) {
        for (Iterator<Resource> it = mavenProject.getResources().iterator(); it.hasNext(); ) {
            Resource next =  it.next();
            if (next.getDirectory().equals(new File(blocksDir).getAbsolutePath())) {
                it.remove();
            }

        }
    }
}
