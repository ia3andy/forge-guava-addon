package org.ia3andy.forge.addon.java.guava.facet;

import javax.inject.Inject;

import org.apache.maven.model.Model;
import org.ia3andy.forge.addon.java.guava.config.GuavaConfiguration;
import org.ia3andy.forge.addon.java.guava.helper.ForgeHelper;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.maven.projects.MavenFacet;
import org.jboss.forge.addon.projects.Project;

public class GuavaFacetImpl extends AbstractFacet<Project> implements GuavaFacet {

    @Inject
    private GuavaConfiguration guavaConfiguration;

    @Override
    public boolean install() {
        ForgeHelper.addPropertyToProject(getFaceted(), guavaConfiguration.getGuavaVersionProperty(), guavaConfiguration.getGuavaVersion());
        ForgeHelper.getOrAddDependency(getFaceted(), "com.google.guava", "guava", "${" + guavaConfiguration.getGuavaVersionProperty() + "}", null, null);
        return isInstalled();
    }

    @Override
    public boolean isInstalled() {
        MavenFacet mavenFacet = getFaceted().getFacet(MavenFacet.class);
        Model pom = mavenFacet.getModel();
        return pom.getProperties().getProperty(guavaConfiguration.getGuavaVersionProperty()) != null;
    }

}
