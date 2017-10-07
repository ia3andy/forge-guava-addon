/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ia3andy.forge.addon.java.guava.config;

import org.jboss.forge.addon.dependencies.Coordinate;

/**
 *
 * @author ia3andy
 */
public interface GuavaConfiguration {

  String getGuavaVersionProperty();
  String getGuavaVersion();

  Coordinate getGuavaCoordinate();

  void setGuavaVersion(String guavaVersion);
}
