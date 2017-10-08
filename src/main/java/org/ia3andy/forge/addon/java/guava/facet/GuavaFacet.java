package org.ia3andy.forge.addon.java.guava.facet;

import org.jboss.forge.addon.projects.ProjectFacet;

/**
 * Facet for Guava dependency check and installation
 */
public interface GuavaFacet extends ProjectFacet {

    /**
     * @return the project Guava dependency version (from pom.xml properties)
     */
    String getGuavaVersion();
}
