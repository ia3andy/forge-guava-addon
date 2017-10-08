package org.ia3andy.forge.addon.java.guava.helper;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

/**
 * Some tools to help with checkNotNull processing on {@link JavaClassSource}
 */
public interface CheckNotNullHelper {

    /**
     * Add the {@link com.google.common.base.Preconditions#checkNotNull(Object)} static import to the given class
     *
     * @param javaClassSource the {@link JavaClassSource} to add the import to
     */
    void addCheckNotNullStaticImportToClass(JavaClassSource javaClassSource);

    /**
     * Check if the given method already has some checkNotNull in its body
     *
     * @param methodSource the {@link MethodSource} to check
     * @return true if it's already there, false nether
     */
    boolean hasMethodCheckNotNull(MethodSource<JavaClassSource> methodSource);

    /**
     * Add checkNotNull on method params
     *
     * @param methodSource the {@link MethodSource} to process
     */
    void addCheckNotNullToMethod(MethodSource<JavaClassSource> methodSource);
}
