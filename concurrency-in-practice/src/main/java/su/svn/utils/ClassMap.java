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

    /**
     * Декоративный метод для добавления значения в карту по ключу.
     *
     * @param key ключ.
     * @param value значение.
     */
    void put(Class<?> key, T value);

    /**
     * Декоративный метод для получения значения в карту по ключу.
     * Если значение есть оно обёрнуто в Optional иначе EMPTY.
     *
     * @param key ключ.
     * @return значение в Optional если есть.
     */
    Optional<T> get(Class<?> key);
}
