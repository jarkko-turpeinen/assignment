package com.library.rest;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * IBM® Cloudant® NoSQL DB for IBM Cloud connector
 */
public final class Cloudant {

    private static URL getUrlObject(String url){
        URL urlObject = null;
        try {
           urlObject = new URL(url);
        } catch (MalformedURLException e) {
           Logger.error(e.getMessage());
        }
        return urlObject;
    }

    /**
     * Creates cloudant client object to return database object
     *
     * Tries to create client by bluemix VCAP credentials and if not found,
     * then by passed url, username and password values
     *
     * @param url
     * @param username
     * @param password
     * @return Database (null if no credentials or url, username and password)
     */
    public static Database getDatabase(String url, String username, String password){
        Logger.debug("getDatabase");
        CloudantClient client = null;
        Database database = null;
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
            } else {
                Logger.info("no database credentials provided");
            }
        }
        // Use first database if multiple are returned
        {
            final String databaseName = client.getAllDbs().get(0);
            if (databaseName != null) {
                database = client.database(databaseName, false);
                Logger.debug("CouchDbInfo [dbName=" + database.info().getDbName() + "]");
            }
        }
        return database;
    }
}
