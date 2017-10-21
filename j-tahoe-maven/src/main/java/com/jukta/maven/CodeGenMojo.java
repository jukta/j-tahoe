package com.jukta.maven;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Iterator;

/**
 * @author Sergey Sidorov
 */
@Mojo(name = "code-gen", requiresDependencyResolution = ResolutionScope.RUNTIME, defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class CodeGenMojo extends AbstractMojo {

    @Parameter( property = "blocksDir", defaultValue = "blocks" )
    private String blocksDir;

    @Parameter( property = "outputDir", defaultValue = "${project.build.directory}/generated-sources" )
    private String outputDir;

    @Parameter( property = "resourcesDir", defaultValue = "public" )
    private String resourcesDir;

    @Parameter( property = "jsDependecies")
    private String jsDependecies;

    @Parameter( property = "cssDependecies")
    private String cssDependecies;

    @Component
    protected MavenProject mavenProject;
    @Component
    protected MavenSession mavenSession;
    @Component
    protected BuildPluginManager pluginManager;
//    protected MojoExecutor.ExecutionEnvironment _pluginEnv;

    public void execute() throws MojoExecutionException, MojoFailureException {

        System.out.println("---------------------");
        System.out.println(jsDependecies);
        System.out.println(cssDependecies);
        System.out.println("---------------------");


        try {
            removeFromResources(blocksDir);
//            removeFromResources(resourcesDir);
            File targetDir = new File(outputDir);
            boolean jar = "jar".equalsIgnoreCase(mavenProject.getPackaging());
            JarBuilder builder;
            if (jar) {
                builder = new JarBuilder(blocksDir, resourcesDir, targetDir, mavenProject, mavenSession, getLog());
            } else {
                builder = new WarBuilder(blocksDir, resourcesDir, targetDir, mavenProject, mavenSession, getLog());
            }

            builder.setJsDependecies(jsDependecies);
            builder.setCssDependecies(cssDependecies);
            builder.generate();

        } catch (Exception e) {
            throw new MojoFailureException("Source generator error", e);
        }
    }

    private void removeFromResources(String dir) {
        for (Iterator<Resource> it = mavenProject.getResources().iterator(); it.hasNext(); ) {
            Resource next =  it.next();
            if (next.getDirectory().equals(new File(dir).getAbsolutePath())) {
                it.remove();
            }

        }
    }
}
