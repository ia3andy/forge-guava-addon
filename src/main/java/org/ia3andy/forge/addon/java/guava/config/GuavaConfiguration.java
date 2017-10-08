/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ia3andy.forge.addon.java.guava.config;

import org.jboss.forge.addon.dependencies.Coordinate;

/**
 * Configuration for Guava addon
 *
 * @author ia3andy
 */
public interface GuavaConfiguration {

    /**
     * @return the Guava version property to use
     */
    String getGuavaVersionProperty();

    /**
     * @return the selected Guava version to use
     */
    String getSelectedGuavaVersion();

    /**
     * @return The Guava base {@link Coordinate} (groupId, artifactId)
     */
    Coordinate getBaseGuavaCoordinate();

    /**
     * @return The Guava {@link Coordinate} to use
     */
    Coordinate getGuavaCoordinateToUse();

    /**
     * @param guavaVersion select the Guava version to use
     */
    void setSelectedGuavaVersion(String guavaVersion);
}
