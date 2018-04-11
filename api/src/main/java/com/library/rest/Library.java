package com.library.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import java.text.DateFormat;

/**
 * REST Service for IOT Equipment metadata maintenance
 */
@ApplicationPath("equipment")
public class Library extends Application {

    /**
     * Maximum number of Equipments to return
     */
    public final static Integer LIMIT_MAX = 10;
    public final static String INVALID_EQUIPMENT_NUMBER = "Parameter Equipment Number is invalid";
    public final static String INVALID_LIMIT = "Parameter Limit is invalid";
    public final static String INVALID_EQUIPMENT = "Parameter Equipment is invalid";

    final static void debug(String details) {
        System.out.print(DateFormat.getDateTimeInstance().format(System.currentTimeMillis()));
        System.out.print(" - ");
        System.out.println(details);
    }

    /**
     * User invokes a GET request from a REST URL to search Equipment
     * that is based on one unique equipment number
     *
     * Example GET URL: http://ServerName/applicationName/equipment/{equipmentNumber}
     *
     * @param equipmentNumber
     * @return JSON object
     *  Equipment in result consists of following attributes:
     *  Equipment Number
     *  Address
     *  Contract Start Date
     *  Contract End Date
     *  Status (Running or Stopped)
     * @throws Exception
     */
    @GET
    @Path("/equipment/{equipmentNumber}")
    @Produces({"application/json"})
    public final static Integer getEquipment(String equipmentNumber) throws Exception {
        debug("getEquipment(" + equipmentNumber + ")");
        try {
            validateParameterEquipmentNumber(equipmentNumber);
        } catch (Exception e) {
            debug(e.getMessage());
            throw new Exception("getEquipment: " + e);
        }
        return 200;
    }

    private final static void validateParameterEquipmentNumber(String equipmentNumber) throws Exception {
        if (equipmentNumber == null || !Equipment.isValidEquipmentNumber(equipmentNumber)) {
            throw new Exception(INVALID_EQUIPMENT_NUMBER);
        }
    }


    /**
     * User invokes a GET request from a REST URL to fetch X Equipment
     * where X is the number of records to fetch
     *
     * Example GET URL: http://ServerName/applicationName/equipment/search?limit={X}
     *
     * @param limit number of records to fetch
     * @return JSON object of Equipments
     *  Each Equipment in result consists of following attributes:
     *  Equipment Number
     *  Address
     *  Contract Start Date
     *  Contract End Date
     *  Status (Running or Stopped)
     *
     * @throws Exception
     */
    @GET
    @Path("/search")
    @PathParam("limit")
    @Produces({"application/json"})
    public final static Integer getEquipments(Integer limit) throws Exception {
        debug("getEquipments(" + limit + ")");
        try {
          validateParameterLimit(limit);
        } catch (Exception e) {
            debug(e.getMessage());
            throw new Exception("getEquipments: " + e.getMessage());
        }
        return 200;
    }

    private final static void validateParameterLimit(Integer limit) throws Exception {
        if (limit == null || limit < 1 | limit > LIMIT_MAX) {
            throw new Exception(INVALID_LIMIT);
        }
    }


    /**
     * User invokes a POST request to REST URL to create Equipment
     * Each POST consists of One Equipment JSON object
     * API validates the input and generates error for duplicate Equipment
     *
     * @param equipment
     *  Equipment consists of following attributes:
     *  Equipment Number
     *  Address
     *  Contract Start Date
     *  Contract End Date
     *  Status (Running or Stopped)
     * @return http code
     * @throws Exception
     */
    @PUT
    @Path("/equipment")
    @Consumes({"application/json"})
    public final static Integer putEquipment(Equipment equipment) throws Exception {
        debug("putEquipment(" + equipment + ")");
        try {
            validateParameterEquipment(equipment);
        } catch (Exception e) {
            debug(e.getMessage());
            throw new Exception("putEquipment: " + e.getMessage());
        }
        return 200;
    }

    private final static void validateParameterEquipment(Equipment equipment) throws Exception {
        if (equipment == null || !equipment.isValid()) {
            throw new Exception(INVALID_EQUIPMENT);
        }
    }
}
