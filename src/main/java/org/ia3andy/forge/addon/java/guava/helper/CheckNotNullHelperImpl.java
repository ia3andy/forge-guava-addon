package org.ia3andy.forge.addon.java.guava.helper;

import static java.text.MessageFormat.format;

import javax.inject.Singleton;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;

@Singleton
public class CheckNotNullHelperImpl implements CheckNotNullHelper {

    private static final Predicate<String> HAS_CHECKNOTNULL_PREDICATE = Pattern.compile(".*\\bcheckNotNull\\([^)]*\\).*", Pattern.MULTILINE).asPredicate();
    private static final String CHECK_NOT_NULL_LINE_FORMAT = "checkNotNull({0});\n";

    @Override
    public boolean hasMethodCheckNotNull(final MethodSource<JavaClassSource> methodSource) {
        return HAS_CHECKNOTNULL_PREDICATE.test(methodSource.getBody());
    }

    @Override
    public void addCheckNotNullToMethod(final MethodSource<JavaClassSource> methodSource) {
        final String body = methodSource.getBody();
        final StringBuilder bodyBuilder = new StringBuilder();
        methodSource.getParameters()
                .stream()
                .filter(p -> !p.getType().isPrimitive())
                .forEach(p -> bodyBuilder.append(getCheckNotNullBodyLineForParam(p)));
        bodyBuilder.append(body);
        methodSource.setBody(bodyBuilder.toString());
    }

    private static String getCheckNotNullBodyLineForParam(final ParameterSource<JavaClassSource> p) {
        return format(CHECK_NOT_NULL_LINE_FORMAT, p.getName());
    }

}
