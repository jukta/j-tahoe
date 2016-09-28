package com.jukta.maven;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;

/**
 * @since 1.0
 */
public class WarBuilder extends JarBuilder {

    public WarBuilder(String blocksDir, String resourceSrcDir, File targetDir, MavenProject mavenProject, MavenSession mavenSession) {
        super(blocksDir, resourceSrcDir, targetDir, mavenProject, mavenSession);
    }

    @Override
    protected void generateProps() throws IOException {

    }
}
