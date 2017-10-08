package org.ia3andy.forge.addon.java.guava.ui;

import javax.inject.Inject;

import org.ia3andy.forge.addon.java.guava.facet.GuavaFacet;
import org.ia3andy.forge.addon.java.guava.helper.CheckNotNullHelper;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.parser.java.beans.ProjectOperations;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Command to add checkNotNull to class methods params
 */
@FacetConstraint({GuavaFacet.class})
public final class GuavaAddCheckNotNullCommand extends AbstractGuavaCommand {

    private final ProjectOperations projectOperations;
    private final CheckNotNullHelper checkNotNullHelper;

    private UISelectOne<JavaResource> targetClass;

    @Inject
    public GuavaAddCheckNotNullCommand(final ProjectFactory projectFactory,
                                       final ProjectOperations projectOperations,
                                       final CheckNotNullHelper checkNotNullHelper) {
        super(projectFactory);
        checkNotNull(projectFactory, "projectFactory must not be null.");
        checkNotNull(projectOperations, "projectOperations must not be null.");
        checkNotNull(checkNotNullHelper, "checkNotNullHelper must not be null.");
        this.projectOperations = projectOperations;
        this.checkNotNullHelper = checkNotNullHelper;
    }

    @Override
    protected String name() {
        return "Add checkNotNull";
    }

    @Override
    protected String description() {
        return "Generate and Add checkNotNull preconditions on constructor/method params for the given class.";
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        checkNotNull(builder, "builder must not be null.");
        final InputComponentFactory inputFactory = builder
                .getInputComponentFactory();
        final UIContext uiContext = builder.getUIContext();
        final Project project = getSelectedProject(uiContext);
        final UISelection<Object> initialSelection = uiContext.getInitialSelection();
        targetClass = inputFactory
                .createSelectOne("targetClass", JavaResource.class)
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
        checkNotNull(context, "context must not be null.");
        try {
            final JavaResource javaResource = targetClass.getValue();
            final JavaClassSource targetClass = javaResource.getJavaType();
            checkNotNullHelper.addCheckNotNullStaticImportToClass(targetClass);
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

    private boolean checkHasNotMethodCheckNotNull(final UIExecutionContext context,
                                                  final MethodSource<JavaClassSource> methodSource) {
        if (checkNotNullHelper.hasMethodCheckNotNull(methodSource)) {
            context.getPrompt().prompt(methodSource.getName() + " already has some checkNotNull");
            return false;
        }
        return true;
    }

}