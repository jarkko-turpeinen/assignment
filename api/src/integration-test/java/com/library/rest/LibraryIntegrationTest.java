package com.library.rest;

import org.junit.*;

import java.util.stream.IntStream;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.rules.ExpectedException;

/**
 * Integration tests for Library
 */
public class LibraryIntegrationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    Library library = new Library();

    final Equipment testEquipment1 = new Equipment("1000000001", null, null, null, "Stopped");
    final Equipment testEquipment2 = new Equipment("1000000002", null, null, null, "Running");

    @Before
    public void before() {
        try {
            Equipment equipment = library.postEquipment(testEquipment1);
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
    }

    @After
    public void after() {
        try {
            library.removeEquipment(testEquipment1);
            library.removeEquipment(testEquipment2);
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
    }

    @Test
    public void postEquipment_duplicate() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.duplicatedEquipment);
        library.postEquipment(new Equipment(testEquipment1.getEquipmentNumber(),null,null,null,"Stopped"));
    }

    @Test
    public void postEquipment()throws Exception {
        Equipment equipment = library.postEquipment(testEquipment2);
        assertThat("getEquipment result", equipment.getEquipmentNumber(), is(testEquipment2.getEquipmentNumber()));
    }

    @Test
    public void getEquipment() throws Exception {
        final Equipment equipment = library.getEquipment(testEquipment1.getEquipmentNumber()).get(0);
        assertThat("getEquipment result", equipment.getEquipmentNumber(), is(testEquipment1.getEquipmentNumber()));
    }

    /**
     * Test all valid limit values (overkill, just for the show)
     */
    @Test
    @Ignore
    public void getEquipments() throws Exception {
        IntStream
            .range(1, Library.limitMax + 1)
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