package com.library.rest;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import java.net.MalformedURLException;
import java.net.URL;

public final class Cloudant {

    private static URL getUrlObject(String url){
        Logger.debug("getDatabase");
        URL urlObject = null;
        try {
           urlObject = new URL(url);
        } catch (MalformedURLException e) {
           Logger.error(e.getMessage());
        }
        return urlObject;
    }

    /**
     * Creates connection to cloudant database
     * First tries to connect by bluemix VCAP credentials
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
        final String credentials = System.getProperty("VCAP_METADATA");
        if (credentials != null) {
            Logger.debug("connect cloudant by bluemix credentials");
            client = ClientBuilder.bluemix(credentials).build();
        } else if (url != null) {
            Logger.debug("connect cloudant by url");
            client = ClientBuilder.url(getUrlObject(url))
                    .username(username)
                    .password(password)
                    .build();
        } else {
            Logger.info("no database connection");
        }
        final String databaseName = client.getAllDbs().get(0);
        if (databaseName != null) {
            Logger.debug("using database " + databaseName);
            database = client.database(databaseName, false);
        }
        return database;
    }
}
