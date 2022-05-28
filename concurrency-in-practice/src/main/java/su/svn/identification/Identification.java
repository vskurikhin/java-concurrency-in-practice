/*
 * This file was last modified at 2022.05.28 21:11 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Identification.java
 * $Id$
 */

package su.svn.identification;

public interface Identification<T> extends Ident {

    /**
     * Возвращает значение идентификатора.
     *
     * @return возвращает значение идентификатора.
     */
    T id();

    /**
     * {@inheritDoc}
     */
    @Override
    default String idString() {
        return id() != null ? id().toString() : null;
    }
}
