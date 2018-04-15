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
@ApplicationPath("api")
@Path("library")
public final class Library extends Application {

    /**
     * Maximum number of Equipments to return
     */
    public final static Integer limitMax = 10;

    /**
     * Validation exceptions
     */
    public final static String invalidEquipmentNumber = "Parameter EquipmentNumber is invalid";
    public final static String invalidLimit = "Parameter Limit is invalid";
    public final static String invalidEquipment = "Parameter Equipment is invalid";

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
            throw new Exception("getEquipment: " + e.getMessage());
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
    @PathParam("limit")
    @Produces({"application/json"})
    public final List<Equipment> getEquipments(Integer limit) throws Exception {
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
            throw new Exception("getEquipments: " + e.getMessage());
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
    @Path("/equipment")
    @Consumes({"application/json"})
    public final Equipment postEquipment(Equipment equipment) throws Exception {
        Logger.debug("postEquipment(" + equipment + ")");
        try {
            validateParameterEquipment(equipment);
            final Database db = getDatabase();
            db.save(equipment);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw new Exception("postEquipment: " + e.getMessage());
        }
        return getEquipment(equipment.getEquipmentNumber()).get(0);
    }

    private static void validateParameterEquipment(Equipment equipment) throws Exception {
        if (equipment == null || !equipment.isValid()) {
            throw new Exception(invalidEquipment);
        }
    }
}
