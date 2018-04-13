package com.library.rest;

import com.cloudant.client.api.Database;
import org.junit.Test;
import java.util.stream.IntStream;
import static com.library.rest.Cloudant.getDatabase;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for Library
 */
public class LibraryI {

    Library library = new Library();

    @Test
    public void postEquipment()throws Exception {
        Equipment equipment = library.postEquipment(new Equipment("1000000003", "127.0.0.1", null, null, "Stopped"));
        assertThat("getEquipment result", equipment.getEquipmentNumber(), is("1000000003"));
    }

    @Test
    public void getEquipment() throws Exception {
        final Equipment equipment = library.getEquipment("1000000001").get(0);
        assertThat("getEquipment result", equipment.getEquipmentNumber(), is("1000000001"));
    }

    /**
     * Test all valid limit values (overkill, just for the show)
     */
    @Test
    public void getEquipments() throws Exception {
        IntStream
            .range(1, Library.LIMIT_MAX + 1)
            .forEach(limit -> {
                try {
                    // To avoid following exception, sleep a few milliseconds
                    // Error: too_many_requests.
                    // Reason: You've exceeded your current limit of 5 requests per second for query class. Please try later..
                    Thread.sleep(250);
                    assertThat("getEquipments by limit", library.getEquipments(limit).size() <= limit , is(true));
                } catch (Exception e) {
                    Logger.debug("getEquipments: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
    }
}