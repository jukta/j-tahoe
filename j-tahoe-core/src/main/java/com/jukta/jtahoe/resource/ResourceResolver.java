package com.jukta.jtahoe.resource;

import java.util.List;

/**
 * @since 1.0
 */
public interface ResourceResolver {

    List<Resource> getResources(ResourceType resourceType);

}
