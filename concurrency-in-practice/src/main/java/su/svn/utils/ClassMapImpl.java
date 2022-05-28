/*
 * This file was last modified at 2022.05.28 21:29 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ClassSwitchMapConsumer.java
 * $Id$
 */

package su.svn.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ClassMapImpl<T> implements ClassMap<T> {
    private final Map<Class<?>, T> initializeMap;

    private volatile Map<Class<?>, T> unmodifiableMap;

    ClassMapImpl() {
        initializeMap = new LinkedHashMap<>();
    }

    public void put(Class<?> key, T value) {
        initializeMap.put(key, value);
    }

    public Optional<T> get(Class<?> key) {
        var localMap = unmodifiableMap;
        if (null == localMap) {
            synchronized (initializeMap) {
                localMap = unmodifiableMap;
                if (null == localMap) {
                    unmodifiableMap = Collections.unmodifiableMap(initializeMap);
                }
            }
        }
        return Optional.ofNullable(unmodifiableMap.get(key));
    }
}