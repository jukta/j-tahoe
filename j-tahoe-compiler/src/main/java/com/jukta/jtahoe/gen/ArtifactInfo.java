package com.jukta.jtahoe.gen;

/**
 * Created by Sergey on 10/19/2017.
 */
public class ArtifactInfo {
    private String groupId;
    private String artifactId;
    private String version;

    public ArtifactInfo(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }
}
