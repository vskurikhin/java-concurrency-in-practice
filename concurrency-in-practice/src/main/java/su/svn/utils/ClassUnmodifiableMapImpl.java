/*
 * This file was last modified at 2022.05.28 21:29 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ClassSwitchMapConsumer.java
 * $Id$
 */

package su.svn.utils;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@ThreadSafe
public class ClassUnmodifiableMapImpl<T> implements ClassMap<T> {

    private final Map<Class<?>, T> initializeMap = new LinkedHashMap<>();

    @GuardedBy("initializeMap")
    private volatile Map<Class<?>, T> unmodifiableMap;

    public void put(Class<?> key, T value) {
        synchronized (initializeMap) {
            if (null == unmodifiableMap) {
                initializeMap.put(key, value);
            }
        }
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