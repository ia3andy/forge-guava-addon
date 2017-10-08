package org.ia3andy.forge.addon.java.guava.facet;

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.inject.Inject;
import java.util.Properties;

import org.apache.maven.model.Model;
import org.ia3andy.forge.addon.java.guava.config.GuavaConfiguration;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.maven.projects.MavenFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import static com.google.common.base.Preconditions.checkNotNull;

public final class GuavaFacetImpl extends AbstractFacet<Project>
		implements
			GuavaFacet {

	private final GuavaConfiguration guavaConfiguration;

	@Inject
	public GuavaFacetImpl(final GuavaConfiguration guavaConfiguration) {
		checkNotNull(guavaConfiguration, "guavaConfiguration must not be null;");
		this.guavaConfiguration = guavaConfiguration;
	}

	@Override
	public boolean install() {
		addGuavaVersionProperty();
		addGuavaDependency();
		return isInstalled();
	}

	@Override
	public boolean isInstalled() {
		return hasGuavaVersionProperty();
	}

	@Override
	public String getGuavaVersion() {
		final MavenFacet mavenFacet = getMavenFacet();
		final Model pom = mavenFacet.getModel();
		return pom.getProperties().getProperty(guavaConfiguration.getGuavaVersionProperty());
	}

	private boolean hasGuavaVersionProperty() {
		return !isNullOrEmpty(getGuavaVersion());
	}

	private void addGuavaDependency() {
		final DependencyFacet dependencies = getDependencyFacet();
		final Dependency dependency = DependencyBuilder.create()
                .setCoordinate(guavaConfiguration.getGuavaCoordinateToUse());
		dependencies.addDirectDependency(dependency);
	}

	private void addGuavaVersionProperty() {
		final MavenFacet maven = getMavenFacet();
		final Model pom = maven.getModel();
		final Properties properties = pom.getProperties();
		properties.setProperty(guavaConfiguration.getGuavaVersionProperty(), guavaConfiguration.getSelectedGuavaVersion());
		maven.setModel(pom);
	}

	private MavenFacet getMavenFacet() {
		return getFaceted().getFacet(MavenFacet.class);
	}

	private DependencyFacet getDependencyFacet() {
		return getFaceted().getFacet(DependencyFacet.class);
	}

}
