package com.library.rest;

import org.junit.Test;
import java.util.stream.IntStream;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for Library
 */
public class LibraryI {

    Library library = new Library();

    @Test
    public void getEquipment() throws Exception {
        assertThat("getEquipment result", library.getEquipment("1"), is(200));
        assertThat("getEquipment result", library.getEquipment("2"), is(200));
    }

    /**
     * Test all valid limit values
     */
    @Test
    public void getEquipments() {
        IntStream
            .range(1, Library.LIMIT_MAX + 1)
            .forEachOrdered(limit -> {
                try {
                    assertThat("getEquipments by limit", library.getEquipments(limit), is(200));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }
}