package org.ia3andy.forge.addon.java.guava.ui;

import javax.inject.Inject;

import org.ia3andy.forge.addon.java.guava.facet.GuavaFacet;
import org.ia3andy.forge.addon.java.guava.helper.CheckNotNullHelper;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.parser.java.beans.ProjectOperations;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UISelection;
import org.jboss.forge.addon.ui.input.InputComponentFactory;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.roaster.model.VisibilityScoped;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

@FacetConstraint({ GuavaFacet.class })
public final class GuavaCheckNotNullCommand extends AbstractGuavaCommand {

	private final ProjectOperations projectOperations;
	private final CheckNotNullHelper checkNotNullHelper;

	private UISelectOne<JavaResource> targetClass;

	@Inject
	public GuavaCheckNotNullCommand(final ProjectOperations projectOperations, final CheckNotNullHelper checkNotNullHelper) {
		this.projectOperations = projectOperations;
		this.checkNotNullHelper = checkNotNullHelper;
	}

	@Override
	protected String name() {
		return "Add checkNotNull";
	}

	@Override
	protected String description() {
		return "Add checkNotNull to check method/contructor params.";
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		final InputComponentFactory inputFactory = builder.getInputComponentFactory();
		final UIContext uiContext = builder.getUIContext();
		final Project project = getSelectedProject(uiContext);
		final UISelection<Object> initialSelection = uiContext.getInitialSelection();

		targetClass = inputFactory.createSelectOne("targetClass", JavaResource.class)
				.setLabel("Target class")
				.setDescription("The class where you want to add checkNotNull.")
				.setRequired(true)
				.setValueChoices(projectOperations.getProjectClasses(project));
		if (initialSelection.get() instanceof JavaResource) {
			targetClass.setValue((JavaResource) initialSelection.get());
		}

		builder.add(targetClass);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		try{
			final JavaResource javaResource = targetClass.getValue();
			final JavaClassSource targetClass = javaResource.getJavaType();
			targetClass.addImport("com.google.common.base.Precondition.checkNotNull").setStatic(true);
			targetClass.getMethods().stream()
					.filter(VisibilityScoped::isPublic)
					.filter(m -> checkHasNotMethodCheckNotNull(context, m))
					.forEach(checkNotNullHelper::addCheckNotNullToMethod);
			getSelectedProject(context).getFacet(JavaSourceFacet.class).saveJavaSource(targetClass);
			return Results.success("Guava > Add checkNotNull: Command successfully executed!");
		} catch (final RuntimeException e) {
			return Results.fail("Guava > Add checkNotNull: Command execution failed!", e);
		}
	}

	private boolean checkHasNotMethodCheckNotNull(final UIExecutionContext context, final MethodSource<JavaClassSource> methodSource) {
		if(checkNotNullHelper.hasMethodCheckNotNull(methodSource)){
			context.getPrompt().prompt(methodSource.getName() + " already has some checkNotNull");
			return false;
		}
		return true;
	}


}