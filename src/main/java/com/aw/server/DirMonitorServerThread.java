package com.aw.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DirMonitorServerThread
 * Describes Directory Watch Server single client handler thread
 *
 * @author Binyamin (Dima) Pyanin
 * @version POC
 * @since March 23, 2022
 */
public class DirMonitorServerThread extends Thread {

    private final Socket socket;
    private final String outputDir;

    public DirMonitorServerThread(Socket socket, final String outputDir) {
        super("DirMonitorServerThread");
        this.socket = socket;
        this.outputDir = outputDir;
    }

    private void log(final String message) {
        System.out.println(
                Thread.currentThread().getName() + "_" +
                        Thread.currentThread().getId() + "::" +
                        message
        );
    }

    @Override
    public void run() {
        try (
                DataInputStream dis = new DataInputStream(socket.getInputStream())
        ) {
            String str = dis.readUTF();

            log("RECEIVED:" + str);

            recreatePropertiesFile(
                    createPropertiesFile(str)
            );

            socket.close();
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    private Map<String, String> createPropertiesFile(final String mapStr) {
        //The message to reconstruct a filtered properties file
        log("Attempting to reconstruct " + mapStr);

        Map<String, String> reconstructedMap =
                Arrays.stream(
                        mapStr.replace("{", "").replace("}", "").split(","))
                        .map(s -> s.split("="))
                        .collect(
                                Collectors.toMap(
                                        s -> s[0].replace(" ", ""),
                                        s -> s[1].replace(" ", "")
                                )
                        );

        log("Reconstructed " + reconstructedMap);

        return reconstructedMap;

    }

    private void recreatePropertiesFile(final Map<String, String> reconstructedMap) {
        //write it to disk, using the original filename
        log("Attempting to write to disk " + reconstructedMap);

        final String pathName = this.outputDir +
                File.separator +
                reconstructedMap.get(ResourceLoader.KEY_ORIGINAL_FILE_NAME);
        log("Destination properties file " + pathName);

        File file = new File(pathName);

        try {
            OutputStream oos = new FileOutputStream(file);

            reconstructedMap.keySet().
                    forEach(
                            e -> {
                                try {
                                    if (!e.equals(ResourceLoader.KEY_ORIGINAL_FILE_NAME)) {
                                        oos.write((e + "=" + reconstructedMap.get(e)).getBytes(StandardCharsets.UTF_8));
                                        oos.write("\n".getBytes(StandardCharsets.UTF_8));
                                    }
                                } catch (IOException e1) {
                                    System.err.println(e1.getLocalizedMessage());
                                }
                            }
                    );

            oos.close();
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }

        log("Created properties file " + pathName);

    }

}
