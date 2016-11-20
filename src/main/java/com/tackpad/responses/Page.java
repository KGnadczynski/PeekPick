package com.tackpad.responses;

import java.util.List;

/**
 * Strona.
 * @author Przemek
 */
public class Page<T> {

    /**
     * Lista.
     */
    public List<T> objectList;

    /**
     * Czy ostatnia
     */
    public boolean isLastPage;
}
