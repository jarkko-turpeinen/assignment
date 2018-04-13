package com.library.rest;
;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.query.QueryBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import java.util.List;
import static com.cloudant.client.api.query.Expression.*;
import static com.library.rest.Cloudant.getDatabase;

/**
 * REST Service for IOT Equipment metadata maintenance in IBM Cloudant NoSQL DB for IBM Cloud
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
    public final static String NO_DATABASE_CONNECTION = "no database connection";

    private final static String URL = "https://28ce2e3f-3a11-428f-a4bb-29ae06964348-bluemix:e7ee215c4a9c5ebf83cad005f7f43d104d7d7cf7ea167974441eaa79534703c2@28ce2e3f-3a11-428f-a4bb-29ae06964348-bluemix.cloudant.com";
    private final static String UID = "28ce2e3f-3a11-428f-a4bb-29ae06964348-bluemix";
    private final static String PWD = "e7ee215c4a9c5ebf83cad005f7f43d104d7d7cf7ea167974441eaa79534703c2";

    /**
     * User invokes a GET request from a REST URL to search Equipment
     * that is based on one unique equipment number
     *
     * Example GET URL: http://ServerName/applicationName/equipment/{equipmentNumber}
     *
     * @param equipmentNumber Equipment identifying number
     * @return List of Equipment documents
     * @throws Exception Invalid Equipment Number parameter or CouchDB exception
     */
    @GET
    @Path("/equipment/{equipmentNumber}")
    @Produces({"application/json"})
    public final List<Equipment> getEquipment(String equipmentNumber) throws Exception {
        Logger.debug("getEquipment(" + equipmentNumber + ")");
        List<Equipment> result = null;
        try {
            validateParameterEquipmentNumber(equipmentNumber);
            Database db = getDatabase(URL, UID, PWD);
            if (db != null) {
                result =
                        db.query(
                                new QueryBuilder(eq("equipmentNumber", equipmentNumber))
                                        .limit(LIMIT_MAX)
                                        .build()
                                , Equipment.class
                        ).getDocs();
            } else {
                throw new Exception(NO_DATABASE_CONNECTION);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw new Exception("getEquipment: " + e.getMessage());
        }
        return result;
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
     * @return List of Equipment documents
     * @throws Exception Invalid parameter Limit or CouchDB exception
     */
    @GET
    @Path("/search")
    @PathParam("limit")
    @Produces({"application/json"})
    public final List<Equipment> getEquipments(Integer limit) throws Exception {
        Logger.debug("getEquipments(" + limit + ")");
        List<Equipment> result = null;
        try {
          validateParameterLimit(limit);
          Database db = getDatabase(URL, UID, PWD);
          if (db != null) {
              result =
                      db.query(
                              new QueryBuilder(ne("equipmentNumber", "null"))
                                      //.sort(Sort.asc("equipmentNumber")) // TODO index equipmentNumber for sorting
                                      .limit(limit)
                                      .build()
                              , Equipment.class
                      ).getDocs();
          } else {
              throw new Exception(NO_DATABASE_CONNECTION);
          }
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw new Exception("getEquipments: " + e.getMessage());
        }
        return result;
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
     * @param equipment Equipment identifying number
     * @return Saved Equipmet document
     * @throws Exception Invalid parameter Equipment or CouchDB exception
     */
    @POST
    @Path("/equipment")
    @Consumes({"application/json"})
    public final Equipment postEquipment(Equipment equipment) throws Exception {
        Logger.debug("postEquipment(" + equipment + ")");
        try {
            validateParameterEquipment(equipment);
            Database db = getDatabase(URL, UID, PWD);
            if (db != null) {
                db.save(equipment);
            } else {
                throw new Exception(NO_DATABASE_CONNECTION);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw new Exception("postEquipment: " + e.getMessage());
        }
        return getEquipment(equipment.getEquipmentNumber()).get(0);
    }

    private static void validateParameterEquipment(Equipment equipment) throws Exception {
        if (equipment == null || !equipment.isValid()) {
            throw new Exception(INVALID_EQUIPMENT);
        }
    }
}
