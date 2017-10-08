package org.ia3andy.forge.addon.java.guava.ui;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

import org.ia3andy.forge.addon.java.guava.config.GuavaConfiguration;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.shell.test.ShellTest;
import org.jboss.forge.addon.ui.test.UITestHarness;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public abstract class AbstractGuavaCommandTest {

    private static final String TESTSET_DIR = "src/test/resources/testset/";
    private static final String PACKAGE_AS_DIR = AbstractGuavaCommandTest.class.getPackage().getName().replace(".", "/");

    protected static final String CLEAN_POM = "clean-pom.xml";
    protected static final String GUAVA_POM = "guava-pom.xml";
    protected static final String GUAVA_NO_PROP_POM = "guava-no-prop-pom.xml";

    protected static final String TEST_SERVICE_JAVA = "TestService.java";

    @Inject
    protected ProjectFactory projectFactory;

    @Inject
    protected FacetFactory facetFactory;

    @Inject
    protected ShellTest shellTest;

    @Inject
    protected GuavaConfiguration guavaConfiguration;

    @Inject
    protected UITestHarness testHarness;

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
                .addClass(AbstractGuavaCommandTest.class)
                .add(newFileAsset(CLEAN_POM), getAssetName(CLEAN_POM))
                .add(newFileAsset(GUAVA_POM), getAssetName(GUAVA_POM))
                .add(newFileAsset(GUAVA_NO_PROP_POM), getAssetName(GUAVA_NO_PROP_POM))
                .add(newFileAsset(TEST_SERVICE_JAVA), getAssetName(TEST_SERVICE_JAVA))
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
        shellTest.clearScreen();
    }

    @After
    public void tearDown() throws Exception {
        shellTest.close();
    }

    protected Project createProjectWithPom(final String pomFileName){
        final Project project = projectFactory.createTempProject();
        facetFactory.install(project, JavaSourceFacet.class);
        final FileResource<?> pom = (FileResource<?>) project.getRoot().getChild("pom.xml");
        if (!pom.getContents().contains("build")) {
            pom.setContents(getClass().getResourceAsStream(pomFileName));
        }
        return refreshedProject(project);
    }

    protected JavaResource saveJavaFileInProject(final Project project, final String javaFileName){
        final JavaClassSource source = Roaster.parse(JavaClassSource.class, getClass().getResourceAsStream(javaFileName));
        return project.getFacet(JavaSourceFacet.class).saveJavaSource(source);
    }

    protected Project refreshedProject(final Project project){
        projectFactory.invalidateCaches();
        return projectFactory.findProject(project.getRoot());
    }

    private static FileAsset newFileAsset(final String fileName){
        return new FileAsset(new File(TESTSET_DIR + fileName));
    }

    private static String getAssetName(final String fileName){
        return PACKAGE_AS_DIR + "/" + fileName;
    }

}
