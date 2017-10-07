package org.ia3andy.forge.addon.java.guava.config;

import javax.inject.Singleton;

@Singleton
public class GuavaConfigurationImpl implements GuavaConfiguration {

  private static final String GUAVA_VERSION_PROPERTY = "guava.version";
  private static final String GUAVA_VERSION = "21.0";

  @Override
  public String getGuavaVersionProperty() {
    return GUAVA_VERSION_PROPERTY;
  }

  @Override
  public String getGuavaVersion() {
    return GUAVA_VERSION;
  }
}
