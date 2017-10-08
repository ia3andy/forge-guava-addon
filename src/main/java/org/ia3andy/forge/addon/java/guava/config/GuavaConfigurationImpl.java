package org.ia3andy.forge.addon.java.guava.config;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import javax.inject.Singleton;

import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;

@Singleton
public final class GuavaConfigurationImpl implements GuavaConfiguration {

  private static final String GUAVA_GROUP_ID = "com.google.guava";
  private static final String GUAVA_ARTIFACT_ID = "guava";
  private static final String GUAVA_VERSION_PROPERTY = "guava.version";
  private static final Coordinate BASE_GUAVA_COORDINATE = CoordinateBuilder.create()
          .setGroupId(GUAVA_GROUP_ID)
          .setArtifactId(GUAVA_ARTIFACT_ID);
  private static final Coordinate GUAVA_COORDINATE_TO_USE = CoordinateBuilder.create(BASE_GUAVA_COORDINATE)
          .setVersion("${" + GUAVA_VERSION_PROPERTY + "}")
          .setPackaging("jar");
  private static final String GUAVA_VERSION = "23.0";

  private String selectedGuavaVersion = GUAVA_VERSION;

  @Override
  public String getGuavaVersionProperty() {
    return GUAVA_VERSION_PROPERTY;
  }

  @Override
  public String getSelectedGuavaVersion() {
    return selectedGuavaVersion;
  }


  @Override
  public void setSelectedGuavaVersion(final String guavaVersion) {
    checkArgument(!isNullOrEmpty(guavaVersion), "guavaVersion must not be null or empty.");
    this.selectedGuavaVersion = guavaVersion;
  }

  @Override
  public Coordinate getBaseGuavaCoordinate() {
    return BASE_GUAVA_COORDINATE;
  }

  @Override
  public Coordinate getGuavaCoordinateToUse() {
    return GUAVA_COORDINATE_TO_USE;

  }
}
