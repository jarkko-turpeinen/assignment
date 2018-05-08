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
 * REST Service assigment-app for IOT Equipment metadata maintenance in IBM Cloudant NoSQL DB for IBM Cloud
 *
 * GET /assigment-app/equipment/{equipmentNumber}
 * GET /assigment-app/equipment/search?limit={[1-1000000]}
 * POST /assigment-app/equipment/{equipmentDocument}
 */
@ApplicationPath("assigment-app")
@Path("/equipment")
public final class Library extends Application {

    /**
     * Maximum number of Equipments to return
     */
    public final static Integer limitMax = 1000000;

    /**
     * Validation exceptions
     */
    public final static String invalidEquipmentNumber = "Equipment Number is invalid";
    public final static String invalidLimit = "Allowed limit range is 1 - " + limitMax;
    public final static String invalidEquipment = "Equipment is invalid";
    public final static String duplicatedEquipment = "Duplicate Equipment Number";

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
    @Path("/{equipmentNumber}")
    @Produces({"application/json"})
    public final List<Equipment> getEquipment(@PathParam("equipmentNumber") String equipmentNumber) throws Exception {
        Logger.debug("getEquipment(" + equipmentNumber + ")");
        List<Equipment> result = null;
        try {
            validateParameterEquipmentNumber(equipmentNumber);
            final Database db = getDatabase();
            if (db != null) {
                result =
                        db.query(
                                new QueryBuilder(eq("equipmentNumber", equipmentNumber))
                                        .limit(limitMax)
                                        .build()
                                , Equipment.class
                        ).getDocs();
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw e;
        }
        return result;
    }

    private final void validateParameterEquipmentNumber(String equipmentNumber) throws Exception {
        if (equipmentNumber == null || !Equipment.isValidEquipmentNumber(equipmentNumber)) {
            throw new Exception(invalidEquipmentNumber);
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
    @QueryParam("limit")
    @Produces({"application/json"})
    public final List<Equipment> getEquipments(@QueryParam("limit") Integer limit) throws Exception {
        Logger.debug("getEquipments(" + limit + ")");
        List<Equipment> result = null;
        try {
          validateParameterLimit(limit);
          final Database db = getDatabase();
          if (db != null) {
              result = db.getAllDocsRequestBuilder()
                      .includeDocs(true)
                      //.sort(Sort.asc("equipmentNumber")) // TODO index equipmentNumber for sorting
                      .limit(limit)
                      .build()
                      .getResponse()
                      .getDocsAs(Equipment.class);
          }
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw e;
        }
        return result;
    }

    private static void validateParameterLimit(Integer limit) throws Exception {
        if (limit == null || limit < 1 | limit > limitMax) {
            throw new Exception(invalidLimit);
        }
    }


    /**
     * User invokes a POST request to REST URL to create Equipment
     * Each POST consists of One Equipment JSON object
     * API validates the input and generates error for duplicate Equipment
     *
     * @param equipment Equipment identifying number
     * @return Equipmet document
     * @throws Exception Invalid parameter Equipment or CouchDB exception
     */
    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    public final Equipment postEquipment(Equipment equipment) throws Exception {
        Logger.debug("postEquipment(" + equipment + ")");
        try {
            validateParameterEquipment(equipment);
            final Database db = getDatabase();
            if (db != null) {
                if (equipment.get_id() != null && equipment.get_rev() != null) {
                    Equipment persisted = db.find(Equipment.class, equipment.get_id(), equipment.get_rev());
                    if (!persisted.getEquipmentNumber().equals(equipment.getEquipmentNumber())) {
                        Logger.debug("equipment number is illegally tampered!");
                        throw new RuntimeException(invalidEquipment);
                    }
                    Logger.debug("update");
                    db.update(equipment);
                } else {
                    Logger.debug("save");
                    getEquipment(equipment.getEquipmentNumber()).forEach(
                            savedEquipment -> {
                                if (savedEquipment.getEquipmentNumber().equals(equipment.getEquipmentNumber())) {
                                    throw new RuntimeException(duplicatedEquipment);
                                }
                            }
                    );
                    db.save(equipment);
                }
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw e;
        }
        return getEquipment(equipment.getEquipmentNumber()).get(0);
    }

    private static void validateParameterEquipment(Equipment equipment) throws Exception {
        if (equipment == null || !equipment.isValid()) {
            throw new Exception(invalidEquipment);
        }
    }

    /**
     * Not a REST endpoint, Used only in intergation testing!
     *
     * @param equipment
     * @throws Exception
     */
    public final void removeEquipment(Equipment equipment) throws Exception {
        Logger.debug("removeEquipment(" + equipment + ")");
        try {
            validateParameterEquipment(equipment);
            final Database db = getDatabase();
            if (db != null) {
                List<Equipment> result = getEquipment(equipment.getEquipmentNumber());
                if (result.size() == 1) {
                    db.remove(result.get(0));
                }
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw e;
        }
    }
}
