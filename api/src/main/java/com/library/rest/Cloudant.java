package com.library.rest;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * IBM Cloudant NoSQL DB for IBM Cloud connector
 */
public final class Cloudant {

    public final static String NO_DATABASE_AVAILABLE = "No database available";

    private static URL getUrlObject(String url) throws Exception {
        URL urlObject = null;
        try {
           urlObject = new URL(url);
        } catch (MalformedURLException e) {
            throw e;
        }
        return urlObject;
    }

    /**
     * Creates Cloudant client object to return database object
     *
     * Tries to create client by bluemix VCAP credentials and if not found,
     * then by passed url, username and password values
     *
     * @param url database url
     * @param username database username
     * @param password password for database user
     * @return Database (null if no Cloud credentials or url, username and password)
     * @throws No database available
     */
    public static Database getDatabase(String url, String username, String password) throws Exception {
        Logger.debug("getDatabase");
        CloudantClient client = null;
        Database database = null;
        try {
            // Credentials for database
            {
                final String credentials = System.getProperty("VCAP_METADATA");
                if (credentials != null) {
                    Logger.debug("IBM Cloudant NoSQL DB for IBM Cloud");
                    client = ClientBuilder.bluemix(credentials).build();
                } else if (url != null) {
                    Logger.debug("From url");
                    client = ClientBuilder.url(getUrlObject(url))
                            .username(username)
                            .password(password)
                            .build();
                }
            }
            // Use first database if multiple are returned
            {
                final String databaseName = client.getAllDbs().get(0);
                if (databaseName != null) {
                    database = client.database(databaseName, false);
                    Logger.debug("[dbName=" + database.info().getDbName() + "]");
                }
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw new Exception(NO_DATABASE_AVAILABLE);
        }
        return database;
    }
}
