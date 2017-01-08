package com.jukta.jtahoe.resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.0
 */
public class CompositeResourceResolver implements ResourceResolver {

    private List<ResourceResolver> resolvers = new ArrayList<>();

    public void addResolver(ResourceResolver resolver) {
        resolvers.add(resolver);
    }

    @Override
    public List<Resource> getResources(ResourceType resourceType) {
        List<Resource> resources = new ArrayList<>();
        for (ResourceResolver resolver : resolvers) {
            resources.addAll(resolver.getResources(resourceType));
        }
        return resources;
    }

}
