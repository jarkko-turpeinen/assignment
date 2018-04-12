package com.library.rest;
;
import com.cloudant.client.api.Database;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;

/**
 * REST Service for IOT Equipment metadata maintenance
 */
@ApplicationPath("equipment")
public final class Library extends Application {

    /**
     * Maximum number of Equipments to return
     */
    public final static Integer LIMIT_MAX = 10;
    public final static String INVALID_EQUIPMENT_NUMBER = "Parameter Equipment Number is invalid";
    public final static String INVALID_LIMIT = "Parameter Limit is invalid";
    public final static String INVALID_EQUIPMENT = "Parameter Equipment is invalid";

    private final static String URL = "https://28ce2e3f-3a11-428f-a4bb-29ae06964348-bluemix:e7ee215c4a9c5ebf83cad005f7f43d104d7d7cf7ea167974441eaa79534703c2@28ce2e3f-3a11-428f-a4bb-29ae06964348-bluemix.cloudant.com";
    private final static String UID = "28ce2e3f-3a11-428f-a4bb-29ae06964348-bluemix";
    private final static String PWD = "e7ee215c4a9c5ebf83cad005f7f43d104d7d7cf7ea167974441eaa79534703c2";

    Database db = Cloudant.getDatabase(URL, UID, PWD);

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
    public final Integer getEquipment(String equipmentNumber) throws Exception {
        Logger.debug("getEquipment(" + equipmentNumber + ")");
        Equipment equipment = null;
        try {
            validateParameterEquipmentNumber(equipmentNumber);
            if (db != null) {
                equipment = db.find(Equipment.class, equipmentNumber);
                Logger.debug(equipment.toString());
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw new Exception("getEquipment: " + e);
        }
        return 200;
    }

    private final void validateParameterEquipmentNumber(String equipmentNumber) throws Exception {
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
    public final Integer getEquipments(Integer limit) throws Exception {
        Logger.debug("getEquipments(" + limit + ")");
        try {
          validateParameterLimit(limit);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw new Exception("getEquipments: " + e.getMessage());
        }
        return 200;
    }

    private static void validateParameterLimit(Integer limit) throws Exception {
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
    public final Integer putEquipment(Equipment equipment) throws Exception {
        Logger.debug("putEquipment(" + equipment + ")");
        try {
            validateParameterEquipment(equipment);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw new Exception("putEquipment: " + e.getMessage());
        }
        return 200;
    }

    private static void validateParameterEquipment(Equipment equipment) throws Exception {
        if (equipment == null || !equipment.isValid()) {
            throw new Exception(INVALID_EQUIPMENT);
        }
    }
}
