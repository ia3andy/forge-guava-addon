package org.ia3andy.forge.addon.java.guava.helper;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

public interface CheckNotNullHelper {
    boolean hasMethodCheckNotNull(MethodSource<JavaClassSource> methodSource);

    void addCheckNotNullToMethod(MethodSource<JavaClassSource> methodSource);
}
