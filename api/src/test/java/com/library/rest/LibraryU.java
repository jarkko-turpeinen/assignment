package com.library.rest;

import com.library.rest.Equipment;
import com.library.rest.Library;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for Library
 */
public class LibraryU {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getEquipments_Limit_null() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_LIMIT);
        Library.getEquipments(null);
    }

    @Test
    public void getEquipments_Limit_overflow() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_LIMIT);
        Library.getEquipments(Library.LIMIT_MAX + 1);
    }

    @Test
    public void getEquipments_Limit_invalid() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_LIMIT);
        Library.getEquipments(0);
    }

    @Test
    public void getEquipment_EquipmentNumber_null() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_EQUIPMENT_NUMBER);
        Library.getEquipment( null);
    }

    @Test
    public void getEquipment_EquipmentNumber_invalid() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_EQUIPMENT_NUMBER);
        Library.getEquipment( "12345678901234567890");
    }

    @Test
    public void putEquipment_Equipment_null() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_EQUIPMENT);
        Library.putEquipment(null);
    }

    @Test
    public void putEquipment_Equipment_invalid() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage(Library.INVALID_EQUIPMENT);
        Library.putEquipment(new Equipment());
    }
}