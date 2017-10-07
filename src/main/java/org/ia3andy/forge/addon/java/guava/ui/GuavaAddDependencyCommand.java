package org.ia3andy.forge.addon.java.guava.ui;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import com.google.common.collect.Iterables;
import org.ia3andy.forge.addon.java.guava.config.GuavaConfiguration;
import org.ia3andy.forge.addon.java.guava.facet.GuavaFacet;
import org.ia3andy.forge.addon.java.guava.helper.GuavaVersionsFinder;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.InputComponentFactory;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;


public final class GuavaAddDependencyCommand extends AbstractGuavaCommand {

	private final GuavaConfiguration configuration;
	private final GuavaVersionsFinder guavaVersionsFinder;

	private UISelectOne<String> guavaVersions;

	@Inject
	public GuavaAddDependencyCommand(final GuavaConfiguration configuration, final GuavaVersionsFinder guavaVersionsFinder) {
		this.configuration = configuration;
		this.guavaVersionsFinder = guavaVersionsFinder;
	}

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
		final List<String> latestGuavaVersions = guavaVersionsFinder.getLatestGuavaVersions();
		InputComponentFactory inputFactory = builder.getInputComponentFactory();

		guavaVersions = inputFactory.createSelectOne("guavaVersion", String.class)
				.setLabel("Guava Version")
				.setDescription("Guava Version, which should be used.")
				.setRequired(true).setRequiredMessage("A version must be selected")
				.setValueChoices(latestGuavaVersions)
				.setDefaultValue(Iterables.getFirst(latestGuavaVersions, configuration.getGuavaVersion()));

		builder.add(guavaVersions);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		try{
			configuration.setGuavaVersion(guavaVersions.getValue());
			Project project = getSelectedProject(context);
			Optional<GuavaFacet> maybeFacet = project.getFacetAsOptional(GuavaFacet.class);
			if(maybeFacet.isPresent()){
				return Results.success("Guava > Add Dependency: Property " + configuration.getGuavaVersionProperty() + " is already in project...");
			}
			factory.install(project, GuavaFacet.class);
			return Results.success("Guava > Add Dependency: Command successfully executed!");
		} catch (final RuntimeException e) {
			return Results.fail("Guava > Add Dependency: Command execution failed!", e);
		}
	}

}