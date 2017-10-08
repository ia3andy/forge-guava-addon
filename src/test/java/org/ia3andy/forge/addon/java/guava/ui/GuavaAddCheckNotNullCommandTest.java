package org.ia3andy.forge.addon.java.guava.ui;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import java.util.concurrent.TimeUnit;

import org.ia3andy.forge.addon.java.guava.helper.CheckNotNullHelper;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.result.Failed;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class GuavaAddCheckNotNullCommandTest extends AbstractGuavaCommandTest {

    @Inject
    private CheckNotNullHelper checkNotNullHelper;

    @Test
    public void checkCommandMetadata() throws Exception {
        final Project guavaProject = createProjectWithPom(GUAVA_POM);
        final JavaResource javaResource = saveJavaFileInProject(guavaProject, TEST_SERVICE_JAVA);
        try (CommandController controller = testHarness.createCommandController(GuavaAddCheckNotNullCommand.class, javaResource)) {
            controller.initialize();
            // Checks the command metadata
            UICommandMetadata metadata = controller.getMetadata();
            assertEquals(GuavaAddCheckNotNullCommand.class, metadata.getType());
            assertEquals("Guava: Add checkNotNull", metadata.getName());
            assertEquals("Guava", metadata.getCategory().getName());
            assertNull(metadata.getCategory().getSubCategory());
            assertEquals(1, controller.getInputs().size());
            assertFalse(controller.hasInput("non existing input"));
            assertTrue(controller.hasInput("targetClass"));
        }
    }

    @Test
    public void shouldAddCheckNotNullToMethodsCorrectly() throws Exception {
        final Project guavaProject = createProjectWithPom(GUAVA_POM);
        final JavaResource javaResource = saveJavaFileInProject(guavaProject, TEST_SERVICE_JAVA);
        try (CommandController controller = testHarness.createCommandController(GuavaAddCheckNotNullCommand.class, javaResource)) {
            controller.initialize();
            final Result result = controller.execute();
            Assert.assertThat(result, not(instanceOf(Failed.class)));
            final JavaClassSource targetClass = javaResource.getJavaType();
            final long count = targetClass.getMethods().stream()
                    .filter(checkNotNullHelper::hasMethodCheckNotNull)
                    .count();
            assertEquals(4, count);
        }
    }

    @Test
    public void shouldAddCheckNotNullToMethodsCorrectlyWithShell() throws Exception {
        final Project guavaProject = createProjectWithPom(GUAVA_POM);
        final JavaResource javaResource = saveJavaFileInProject(guavaProject, TEST_SERVICE_JAVA);
        shellTest.getShell().setCurrentResource(javaResource);
        final Result result = shellTest.execute("guava-add-checknotnull", 15, TimeUnit.SECONDS);
        Assert.assertThat(result, not(instanceOf(Failed.class)));
        final JavaClassSource targetClass = javaResource.getJavaType();
        final long count = targetClass.getMethods().stream()
                .filter(checkNotNullHelper::hasMethodCheckNotNull)
                .count();
        assertEquals(4, count);
    }


}