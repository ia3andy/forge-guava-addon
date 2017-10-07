package org.ia3andy.forge.addon.java.guava.ui;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.ia3andy.forge.addon.java.guava.config.GuavaConfiguration;
import org.ia3andy.forge.addon.java.guava.facet.GuavaFacet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.shell.test.ShellTest;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.result.Failed;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.test.UITestHarness;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GuavaAddDependencyCommandTest {

	@Inject
	private ProjectFactory projectFactory;

	@Inject
	private UITestHarness testHarness;

	@Inject
	private ShellTest shellTest;

	@Inject
	private GuavaConfiguration guavaConfiguration;

	private Project project;

	@Deployment
	@AddonDependencies({
			@AddonDependency(name = "org.jboss.forge.furnace.container:cdi"),
			@AddonDependency(name = "org.jboss.forge.addon:parser-java"),
			@AddonDependency(name = "org.jboss.forge.addon:ui-test-harness"),
			@AddonDependency(name = "org.jboss.forge.addon:shell-test-harness"),
			@AddonDependency(name = "org.jboss.forge.addon:projects"),
			@AddonDependency(name = "org.jboss.forge.addon:maven"),
			@AddonDependency(name = "org.ia3andy.forge.addon:guava-addon")
	})
	public static AddonArchive getDeployment() {
		return ShrinkWrap
				.create(AddonArchive.class)
				.addBeansXML()
				.add(new FileAsset(new File("src/test/resources/testset/test-pom.xml")),
						"org/ia3andy/forge/addon/java/guava/ui/test-pom.xml")
				.addAsAddonDependencies(
						AddonDependencyEntry.create("org.jboss.forge.furnace.container:cdi"),
						AddonDependencyEntry.create("org.jboss.forge.addon:projects"),
						AddonDependencyEntry.create("org.jboss.forge.addon:parser-java"),
						AddonDependencyEntry.create("org.jboss.forge.addon:shell-test-harness"),
						AddonDependencyEntry.create("org.jboss.forge.addon:ui-test-harness"),
						AddonDependencyEntry.create("org.jboss.forge.addon:maven"),
						AddonDependencyEntry.create("org.ia3andy.forge.addon:guava-addon")
				);
	}

	@Before
	public void setUp() throws IOException {
		project = projectFactory.createTempProject();
		FileResource<?> pom = (FileResource<?>) project.getRoot().getChild("pom.xml");
		if (!pom.getContents().contains("build")) {
			pom.setContents(getClass().getResourceAsStream("test-pom.xml"));
		}
		shellTest.clearScreen();
	}

	@After
	public void tearDown() throws Exception {
		shellTest.close();
	}

	@Test
	public void checkCommandMetadata() throws Exception {
		try (CommandController controller = testHarness.createCommandController(GuavaAddDependencyCommand.class, project.getRoot())) {
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
	public void shouldAddGuavaDependencyOnSamplePomCorrectly() throws Exception {
		try (CommandController controller = testHarness.createCommandController(GuavaAddDependencyCommand.class, project.getRoot())) {
			Assert.assertFalse(project.hasFacet(GuavaFacet.class));
			controller.initialize();
			controller.execute();

			Assert.assertTrue(project.hasFacet(GuavaFacet.class));
		}
	}

	@Test
	public void shouldAddGuavaDependencyOnSamplePomCorrectlyWithShell() throws Exception {
		Assert.assertFalse(project.hasFacet(GuavaFacet.class));
		shellTest.getShell().setCurrentResource(project.getRoot());
		Result result = shellTest.execute("guava-add-dependency", 15, TimeUnit.SECONDS);
		Assert.assertThat(result, not(instanceOf(Failed.class)));
		project = projectFactory.findProject(project.getRoot());
		Assert.assertTrue(project.hasFacet(GuavaFacet.class));
	}
}