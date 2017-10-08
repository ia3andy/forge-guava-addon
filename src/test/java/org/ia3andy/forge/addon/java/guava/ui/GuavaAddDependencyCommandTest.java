package org.ia3andy.forge.addon.java.guava.ui;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.ia3andy.forge.addon.java.guava.facet.GuavaFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.result.Failed;
import org.jboss.forge.addon.ui.result.Result;
import org.junit.Assert;
import org.junit.Test;

public class GuavaAddDependencyCommandTest extends AbstractGuavaCommandTest {

	@Test
	public void checkCommandMetadata() throws Exception {
		final Project cleanProject = createProjectWithPom(CLEAN_POM);
		try (CommandController controller = testHarness.createCommandController(GuavaAddDependencyCommand.class, cleanProject.getRoot())) {
			controller.initialize();
			// Checks the command metadata
			UICommandMetadata metadata = controller.getMetadata();
			assertEquals(metadata.getType(), GuavaAddDependencyCommand.class);
			assertEquals("Guava: Add Dependency", metadata.getName());
			assertEquals("Guava", metadata.getCategory().getName());
			assertNull(metadata.getCategory().getSubCategory());
			assertEquals(1, controller.getInputs().size());
			assertFalse(controller.hasInput("non existing input"));
			assertTrue(controller.hasInput("guavaVersion"));
		}
	}

	@Test
	public void shouldAddGuavaDependencyOnCleanPomCorrectly() throws Exception {
		final Project cleanProject = createProjectWithPom(CLEAN_POM);
		try (CommandController controller = testHarness.createCommandController(GuavaAddDependencyCommand.class, cleanProject.getRoot())) {
			Assert.assertFalse(cleanProject.hasFacet(GuavaFacet.class));
			controller.initialize();
			controller.execute();
			final Project refreshedProject = refreshedProject(cleanProject);
			Assert.assertTrue(refreshedProject.hasFacet(GuavaFacet.class));
		}
	}

	@Test
	public void shouldAddGuavaDependencyOnCleanPomCorrectlyWithShell() throws Exception {
		final Project cleanProject = createProjectWithPom(CLEAN_POM);
		Assert.assertFalse(cleanProject.hasFacet(GuavaFacet.class));
		shellTest.getShell().setCurrentResource(cleanProject.getRoot());
		Result result = shellTest.execute("guava-add-dependency", 15, TimeUnit.SECONDS);
		Assert.assertThat(result, not(instanceOf(Failed.class)));
		final Project refreshedProject = refreshedProject(cleanProject);
		Assert.assertTrue(refreshedProject.hasFacet(GuavaFacet.class));
	}

	@Test
	public void shouldDoNothingAndSucceedOnGuavaPom() throws Exception {
		final Project guavaProject = createProjectWithPom(GUAVA_POM);
		final Optional<GuavaFacet> facetAsOptional = guavaProject.getFacetAsOptional(GuavaFacet.class);
		Assert.assertTrue(facetAsOptional.isPresent());
		Assert.assertEquals("18.0", facetAsOptional.get().getGuavaVersion());
		try (CommandController controller = testHarness.createCommandController(GuavaAddDependencyCommand.class, guavaProject.getRoot())) {
			controller.initialize();
			controller.execute();
			final Project refreshedProject = refreshedProject(guavaProject);
			final Optional<GuavaFacet> facetAfterInstallAsOptional = refreshedProject.getFacetAsOptional(GuavaFacet.class);
			Assert.assertTrue(facetAfterInstallAsOptional.isPresent());
			Assert.assertEquals("18.0", facetAfterInstallAsOptional.get().getGuavaVersion());
		}
	}

	@Test
	public void shouldOverrideVersionOnGuavaNoPropPom() throws Exception {
		final Project guavaNoPropProject = createProjectWithPom(GUAVA_NO_PROP_POM);
		final Optional<GuavaFacet> facetAsOptional = guavaNoPropProject.getFacetAsOptional(GuavaFacet.class);
		Assert.assertFalse(facetAsOptional.isPresent());
		try (CommandController controller = testHarness.createCommandController(GuavaAddDependencyCommand.class, guavaNoPropProject.getRoot())) {
			controller.initialize();
			controller.execute();
			final Project refreshedProject = refreshedProject(guavaNoPropProject);
			Assert.assertTrue(refreshedProject.hasFacet(GuavaFacet.class));
		}
	}
}