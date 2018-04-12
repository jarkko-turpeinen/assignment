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
        thrown.expectMessage(Library.INVALID_LIMIT);
        library.getEquipments(null);
    }

    @Test
    public void getEquipments_Limit_overflow() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_LIMIT);
        library.getEquipments(Library.LIMIT_MAX + 1);
    }

    @Test
    public void getEquipments_Limit_invalid() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_LIMIT);
        library.getEquipments(0);
    }

    @Test
    public void getEquipment_EquipmentNumber_null() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_EQUIPMENT_NUMBER);
        library.getEquipment( null);
    }

    @Test
    public void getEquipment_EquipmentNumber_invalid() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_EQUIPMENT_NUMBER);
        library.getEquipment( "12345678901234567890");
    }

    @Test
    public void postEquipment_Equipment_null() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_EQUIPMENT);
        library.postEquipment(null);
    }

    @Test
    public void postEquipment_Equipment_invalid() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_EQUIPMENT);
        library.postEquipment(new Equipment());
    }
}