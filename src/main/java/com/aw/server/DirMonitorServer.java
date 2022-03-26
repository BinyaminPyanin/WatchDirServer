package com.aw.server;


import java.io.IOException;
import java.net.ServerSocket;

/**
 * DirMonitorServer
 * Describes Directory Watch Server that accepts messages from clients.
 * <p>
 * It should be capable of handling messages sent by multiple clients simultaneously.
 * <p>
 * Upon receipt of a message from a client, the server should use the message to reconstruct a filtered properties file
 * and write it to disk, using the original filename.
 * <p>
 * The server program’s main method should accept an argument specifying a config file path.
 * The server config file should contain values defining:
 * <p>
 * a)the location of the directory to which to write the files
 * b)what port to listen on
 * c)any other value(s) you think should be configurable
 *
 * @author Binyamin (Dima) Pyanin
 * @version POC
 * @since March 23, 2022
 */
public class DirMonitorServer {
    private final String outputDir;
    private final int portNumber;

    public DirMonitorServer(String configFilePath) throws Exception {

        ServerResourceLoader resourceLoader = new ServerResourceLoader();

        resourceLoader.readAllProperties(configFilePath);

        if (!resourceLoader.getProperties().isEmpty()) {
            this.outputDir = (String) resourceLoader.getProperties().get(ServerResourceLoader.KEY_DIR_OUTPUT_PATH);
            System.out.println("Output directory = " + this.outputDir);

            this.portNumber = Integer.parseInt((String) resourceLoader.getProperties().get(ServerResourceLoader.KEY_SERVER_PORT));
            System.out.println("Server port = " + this.portNumber);

            System.out.println(" Directory Monitoring Service Listening On Port " + this.portNumber);
        } else {
            throw new Exception("No properties loaded.Unable to start the server");
        }

    }

    private void startServer() {
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(this.portNumber)) {
            while (listening) {
                new DirMonitorServerThread(serverSocket.accept(), this.outputDir).start();
            }
        } catch (IOException e) {
            System.err.println("Server Could Not Listen On Port " + this.portNumber);
            System.exit(-1);
        }
    }

    static void usage() {
        System.err.println("Usage: java DirMonitorServer [-r] config_file");
        System.exit(-1);
    }

    public static void main(String[] args) throws Exception {
        // parse arguments
        if (args.length != 1) {
            usage();
        }

        // register directory and process its events
        new DirMonitorServer(args[0]).startServer();
    }

}
