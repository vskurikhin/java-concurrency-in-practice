/*
 * This file was last modified at 2022.05.28 21:59 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ClassSwitchMap.java
 * $Id$
 */

package su.svn.utils;

import java.util.Optional;

public interface ClassMap<T> {

    void put(Class<?> key, T value);

    Optional<T> get(Class<?> key);
}
