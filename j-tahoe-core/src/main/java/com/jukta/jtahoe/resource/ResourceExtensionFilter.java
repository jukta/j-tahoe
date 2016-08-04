package com.jukta.jtahoe.resource;

/**
 * @since 1.0
 */
public class ResourceExtensionFilter implements ResourceFilter {

    private ResourceType type;

    public ResourceExtensionFilter(ResourceType type) {
        this.type = type;
    }

    @Override
    public boolean accept(Resource resource) {
        return resource.getName().endsWith("." + type.getExtension());
    }
}
