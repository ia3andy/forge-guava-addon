package org.ia3andy.forge.addon.java.guava.helper;

import java.util.List;

/**
 * Some tools to help find lastest Guava version
 */
public interface GuavaVersionsFinder {

    /**
     * @return a list of the latest released Guava versions
     */
    List<String> getLatestGuavaReleasedVersions();
}
