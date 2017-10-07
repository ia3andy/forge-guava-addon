package org.ia3andy.forge.addon.java.guava.config;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import javax.inject.Singleton;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;

@Singleton
public final class GuavaConfigurationImpl implements GuavaConfiguration {

  private static final String GUAVA_GROUP_ID = "com.google.guava";
  private static final String GUAVA_ARTIFACT_ID = "guava";
  private static final String GUAVA_VERSION_PROPERTY = "guava.version";
  private static final String GUAVA_VERSION = "23.0";

  private String guavaVersion = GUAVA_VERSION;

  @Override
  public String getGuavaVersionProperty() {
    return GUAVA_VERSION_PROPERTY;
  }

  @Override
  public String getGuavaVersion() {
    return guavaVersion;
  }

  @Override
  public void setGuavaVersion(final String guavaVersion) {
    checkArgument(!isNullOrEmpty(guavaVersion), "guavaVersion must not be null or empty.");
    this.guavaVersion = guavaVersion;
  }

  @Override
  public Coordinate getGuavaCoordinate() {
    return CoordinateBuilder.create()
            .setGroupId(GUAVA_GROUP_ID)
            .setArtifactId(GUAVA_ARTIFACT_ID)
            .setVersion("${" + GUAVA_VERSION_PROPERTY + "}")
            .setPackaging("jar");
  }
}
