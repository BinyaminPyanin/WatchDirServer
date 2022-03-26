package com.aw.server;

/**
 * ServerResourceLoader
 * Describes Properties File Loader/Processor For Directory Watch Server
 * <p>
 * Provides set of methods to:
 * <p>
 * 1) Read the properties file into a Map
 * 2) Apply a regular expression pattern filter for the keys
 * (i.e., remove key/value mappings where keys do not match a configurable regular expression pattern).
 * 3) Delete the file
 *
 * @author Binyamin (Dima) Pyanin
 * @version POC
 * @since March 23, 2022
 */
public class ServerResourceLoader extends ResourceLoader {

    public final static String KEY_DIR_OUTPUT_PATH = "dir.output.path";
    public final static String KEY_SERVER_PORT = "server.port";

}
