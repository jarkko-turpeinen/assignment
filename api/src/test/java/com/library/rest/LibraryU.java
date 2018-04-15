package com.library.rest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for Library
 */
public class LibraryU {

    Library library = new Library();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getEquipments_Limit_null() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.invalidLimit);
        library.getEquipments(null);
    }

    @Test
    public void getEquipments_Limit_overflow() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.invalidLimit);
        library.getEquipments(Library.limitMax + 1);
    }

    @Test
    public void getEquipments_Limit_invalid() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.invalidLimit);
        library.getEquipments(0);
    }

    @Test
    public void getEquipment_EquipmentNumber_null() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.invalidEquipmentNumber);
        library.getEquipment( null);
    }

    @Test
    public void getEquipment_EquipmentNumber_invalid() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.invalidEquipmentNumber);
        library.getEquipment( "12345678901234567890");
    }

    @Test
    public void postEquipment_Equipment_null() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.invalidEquipment);
        library.postEquipment(null);
    }

    @Test
    public void postEquipment_Equipment_invalid() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.invalidEquipment);
        library.postEquipment(new Equipment());
    }
}