package org.ia3andy.forge.addon.java.guava.helper;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.ia3andy.forge.addon.java.guava.config.GuavaConfiguration;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.DependencyQuery;
import org.jboss.forge.addon.dependencies.DependencyResolver;
import org.jboss.forge.addon.dependencies.builder.DependencyQueryBuilder;

@Singleton
public final class GuavaVersionsFinderImpl implements GuavaVersionsFinder {

    private static final Predicate<String> GUAVA_RELEASE_VERSION_PREDICATE = Pattern
            .compile("^\\d+\\.\\d+$").asPredicate();

    private final DependencyResolver resolver;
    private final GuavaConfiguration configuration;

    @Inject
    public GuavaVersionsFinderImpl(final DependencyResolver resolver,
                                   final GuavaConfiguration configuration) {
        checkNotNull(resolver, "resolver must not be null.");
        checkNotNull(configuration, "configuration must not be null.");
        this.resolver = resolver;
        this.configuration = configuration;
    }

    @Override
    public List<String> getLatestGuavaReleasedVersions() {
        final DependencyQuery dependencyQuery = DependencyQueryBuilder.create(configuration.getBaseGuavaCoordinate())
                .setFilter(d -> GUAVA_RELEASE_VERSION_PREDICATE.test(d.getCoordinate().getVersion()));
        return resolver.resolveVersions(dependencyQuery).stream()
                .map(Coordinate::getVersion)
                .sorted(Comparator.reverseOrder())
                .limit(5)
                .collect(Collectors.toList());
    }
}
