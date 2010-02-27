/*
 * Copyright (c) 2010 Victor Volle.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Contributors:
 *      Victor Volle
 */

package de.artive.visiograph.helper;

import de.artive.visiograph.VisioGraphException;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 15, 2010 Time: 7:47:43 PM To change this template use File | Settings
 * | File Templates.
 */
public class CreateInstance {
  public static <T> T createInstance(String loaderName) {
    T instance;
    try {
      Class<T> clazz = (Class<T>) Class.forName(loaderName);
      instance = clazz.newInstance();
    } catch (ClassNotFoundException e) {
      throw new VisioGraphException("could not find loader class: " + loaderName);
    } catch (InstantiationException e) {
      throw new VisioGraphException(
          "could not instanttiate instance of: " + loaderName + " perhaps a no-arg constructor is missing?)");
    } catch (IllegalAccessException e) {
      throw new VisioGraphException(
          "could not instanttiate instance of: " + loaderName + " perhaps the no-arg constructor is not public?)");
    }
                                                                        
    return instance;
  }

}
