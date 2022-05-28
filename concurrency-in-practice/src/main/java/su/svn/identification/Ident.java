/*
 * This file was last modified at 2022.05.28 21:07 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Identification.java
 * $Id$
 */

package su.svn.identification;

import java.io.Serializable;

public interface Ident extends Serializable {

    /**
     * Возвращает строковое представление идентификатора.
     *
     * @return возвращает строковое представление идентификатора.
     */
    String idString();

    /**
     * Возвращает класс идентификатора.
     *
     * @return возвращает класс идентификатора.
     */
    Class<?> idClass();
}
