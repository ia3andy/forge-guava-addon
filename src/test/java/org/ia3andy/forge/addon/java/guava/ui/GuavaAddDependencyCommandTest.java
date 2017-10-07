package org.ia3andy.forge.addon.java.guava.ui;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.maven.model.Model;
import org.ia3andy.forge.addon.java.guava.config.GuavaConfiguration;
import org.ia3andy.forge.addon.java.guava.facet.GuavaFacet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.maven.projects.MavenFacet;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
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
			pom.setContents("<?xml version=\"1.0\"?>\n" +
					"<project\n" +
					"        xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
					"        xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
					"    <modelVersion>4.0.0</modelVersion>\n" +
					"    <groupId>com.cdi.crud</groupId>\n" +
					"    <artifactId>cdi-crud</artifactId>\n" +
					"    <version>4.0.0</version>\n" +
					"    <packaging>war</packaging>\n" +
					"    <name>cdi-crud</name>\n" +
					"\n" +
					"    <dependencies>\n" +
					"        <dependency>\n" +
					"            <groupId>javax</groupId>\n" +
					"            <artifactId>javaee-api</artifactId>\n" +
					"            <version>7.0</version>\n" +
					"            <scope>provided</scope>\n" +
					"        </dependency>\n" +
					"    </dependencies>\n" +
					"    <build>\n" +
					"        <finalName>cdi-crud</finalName>\n" +
					"    </build>\n" +
					"</project>\n");
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
			assertTrue(controller.getCommand() instanceof GuavaAddDependencyCommand);
			UICommandMetadata metadata = controller.getMetadata();
			assertEquals("Guava: Add Dependency", metadata.getName());
			assertEquals("Guava", metadata.getCategory().getName());
			assertNull(metadata.getCategory().getSubCategory());
			assertEquals(0, controller.getInputs().size());
		}
	}

	@Test
	public void shouldAddGuavaDependencyOnSamplePomCorrectly() throws Exception {
		try (CommandController controller = testHarness.createCommandController(GuavaAddDependencyCommand.class, project.getRoot())) {

			controller.initialize();
			controller.execute();

			GuavaFacet guavaFacet = project.getFacet(GuavaFacet.class);
			Assert.assertTrue(guavaFacet.isInstalled());
		}
	}

	@Test
	public void shouldAddGuavaDependencyOnSamplePomCorrectlyWithShell() throws Exception {
		shellTest.getShell().setCurrentResource(project.getRoot());
		Result result = shellTest.execute("guava-add-dependency", 15, TimeUnit.SECONDS);
		Assert.assertThat(result, not(instanceOf(Failed.class)));
		project = projectFactory.findProject(project.getRoot());
		Assert.assertTrue(project.hasFacet(GuavaFacet.class));
	}
}