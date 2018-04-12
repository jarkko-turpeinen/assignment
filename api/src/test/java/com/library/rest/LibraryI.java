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

    private final static String URL = "https://28ce2e3f-3a11-428f-a4bb-29ae06964348-bluemix:e7ee215c4a9c5ebf83cad005f7f43d104d7d7cf7ea167974441eaa79534703c2@28ce2e3f-3a11-428f-a4bb-29ae06964348-bluemix.cloudant.com";
    private final static String UID = "28ce2e3f-3a11-428f-a4bb-29ae06964348-bluemix";
    private final static String PWD = "e7ee215c4a9c5ebf83cad005f7f43d104d7d7cf7ea167974441eaa79534703c2";

    Database db = getDatabase(URL, UID, PWD);

    @Test
    public void postEquipment() {
        db.save(new Equipment("1000000003", "127.0.0.1", null, null, "Stopped"));
    }

    @Test
    public void getEquipment() throws Exception {
        final String equipmentNumber = library.getEquipment("1000000001").get(0).getEquipmentNumber();
        assertThat("getEquipment result", equipmentNumber, is("1000000001"));
    }

    /**
     * Test all valid limit values (overkill, just for the show)
     */
    @Test
    public void getEquipments() {
        IntStream
            .range(1, Library.LIMIT_MAX + 1)
            .forEach(limit -> {
                try {
                    // To avoid following exception sleep a few milliseconds
                    // Error: too_many_requests.
                    // Reason: You've exceeded your current limit of 5 requests per second for query class. Please try later..
                    Thread.sleep(250);
                    assertThat("getEquipments by limit", library.getEquipments(limit), is(200));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }
}