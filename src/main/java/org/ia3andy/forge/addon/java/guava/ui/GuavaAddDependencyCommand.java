package org.ia3andy.forge.addon.java.guava.ui;

import java.util.Optional;

import org.ia3andy.forge.addon.java.guava.facet.GuavaFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.result.Result;

@FacetConstraint({ GuavaFacet.class })
public class GuavaAddDependencyCommand extends AbstractGuavaCommand {

	@Override
	protected String name() {
		return "Add Dependency";
	}

	@Override
	protected String description() {
		return "Add Guava Dependency.";
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {

	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		Project project = getSelectedProject(context);
		Optional<GuavaFacet> maybeFacet = project.getFacetAsOptional(GuavaFacet.class);

		final GuavaFacet facet = maybeFacet
				.orElseGet(() -> factory.install(project, GuavaFacet.class));

		return success();
	}



}