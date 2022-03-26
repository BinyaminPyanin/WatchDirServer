# Watch Directory Server

## Description

Corresponding server program that accepts messages from clients. 
It should be capable of handling messages sent by multiple clients simultaneously.

Upon receipt of a message from a client, 
the server should use the message to reconstruct a 
filtered properties file and write it to disk, using the original filename.

### Tech stack/Best practices implemented

- [SpringBoot ```2.6.4```](https://spring.io/projects/spring-boot)
- Java 1.8
- [Maven wrapper](https://github.com/takari/maven-wrapper)

## Running locally

The project includes a Maven wrapper ```mvnw```. So no build tool needs to be installed.

To build run:

```./mvnw clean install```

To start server run:

```java -cp target/WatchDirServer-1.0.0.jar com.aw.server.DirMonitorServer  <server_config_properties_file_location_path>```

For example:

```server-config.properties``` located in ```./src/main/resources/server```

```java -cp target/WatchDirServer-1.0.0.jar com.aw.server.DirMonitorServer ./src/main/resources/server/```

To stop server:

```CTRL + c```

## Update maven dependencies to the latest version

- To update maven dependencies , run:

```./mvnw versions:display-dependency-updates```


## Have fun :-)

