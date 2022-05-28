/*
 * This file was last modified at 2022.05.28 21:12 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * IdentificationLong.java
 * $Id$
 */

package su.svn.identification;

public interface IdentificationLong extends Ident {

    long id();

    /**
     * {@inheritDoc}
     */
    @Override
    default String idString() {
        return Long.toString(id());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Class<?> idClass() {
        return long.class;
    }
}