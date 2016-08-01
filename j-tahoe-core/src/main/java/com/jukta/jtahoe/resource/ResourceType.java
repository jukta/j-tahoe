package com.jukta.jtahoe.resource;

/**
 * @since 1.0
 */
public enum ResourceType {
    XML("xml"),
    CSS("css"),
    JS("js");

    private String extension;

    ResourceType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
