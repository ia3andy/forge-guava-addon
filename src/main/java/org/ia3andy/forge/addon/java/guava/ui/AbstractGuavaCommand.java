package org.ia3andy.forge.addon.java.guava.ui;

import javax.inject.Inject;

import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.maven.projects.MavenFacet;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

@FacetConstraint({ MavenFacet.class })
public abstract class AbstractGuavaCommand extends AbstractProjectCommand {

    private static final String GUAVA_CATEGORY = "Guava";

    @Inject
    protected FacetFactory factory;

    @Inject
    protected ProjectFactory projectFactory;

    abstract protected String name();

    abstract protected String description();

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(this.getClass())
            .name(GUAVA_CATEGORY + ": " + name())
            .description(description())
            .category(Categories.create(GUAVA_CATEGORY));
    }


    @Override
    protected boolean isProjectRequired() {
        return true;
    }

    @Override
    protected ProjectFactory getProjectFactory() {
        return projectFactory;
    }

    protected Result success() {
        return Results
                .success("Command '" + name() + "' successfully executed!");
    }

}
