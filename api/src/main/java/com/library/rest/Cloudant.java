package com.library.rest;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * IBM Cloudant NoSQL DB for IBM Cloud connector
 */
public final class Cloudant {

    public final static String noDatabaseAvailable = "No database available";
    private final static String propertiesFile = "cloudant.properties";

    private static Properties getProperties() throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = Cloudant.class.getClassLoader().getResourceAsStream(propertiesFile);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw e;
        }
        return properties;
    }

    /**
     * Creates Cloudant client object to return database object
     *
     * Tries to create client by bluemix VCAP_SERVICES credentials and if not found,
     * then from properties url, username and password values
     *
     * @return Database (null if no Cloud credentials or url, username and password)
     * @throws Exception Exception No database available
     */
    public static Database getDatabase() throws Exception {
        Logger.debug("getDatabase");
        CloudantClient client = null;
        Database database = null;
        try {
            // Credentials for database
            {
                final String credentials = System.getenv("VCAP_SERVICES");
                if (credentials != null) {
                    Logger.debug("IBM Cloudant NoSQL DB for IBM Cloud");
                    client = ClientBuilder.bluemix(credentials).build();
                } else {
                    final String url = getProperties().getProperty("url");
                    if (url != null) {
                        Logger.debug("From properties");
                        client = ClientBuilder.url(new URL(url)).build();
                    }
                }
            }
            // Set database
            {
                final String dbName = getProperties().getProperty("dbName");
                if (client.getAllDbs().contains(dbName)) {
                    database = client.database(dbName, false);
                    Logger.debug("[dbName=" + database.info().getDbName() + "]");
                } else {
                    Logger.debug("database " + dbName + " not found");
                }
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw new Exception(noDatabaseAvailable);
        }
        return database;
    }
}
