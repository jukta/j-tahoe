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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceExtensionFilter that = (ResourceExtensionFilter) o;

        return type == that.type;

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
