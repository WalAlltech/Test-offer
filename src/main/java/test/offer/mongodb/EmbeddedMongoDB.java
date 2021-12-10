package test.offer.mongodb;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.Defaults;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.distribution.Version.Main;
import de.flapdoodle.embed.process.config.RuntimeConfig;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

@Slf4j
public class EmbeddedMongoDB {

    private MongodProcess mongodProcess;
    private String host = "localhost";
    private Main version = Main.DEVELOPMENT;
    private int port = 29019;
    private boolean active;
    
    /**
     * Creates a new EmbeddedMongoDB instance 
     * 
     * @return EmbeddedMongoDB instance 
     */
    public static EmbeddedMongoDB create() {
        return new EmbeddedMongoDB();
    }

    /**
     * Sets the port for the EmbeddedMongoDB instance 
     * 
     * Default is 29019
     * 
     * @param port The port to set
     * @return EmbeddedMongoDB instance 
     */
    public EmbeddedMongoDB withPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * Sets the host for the EmbeddedMongoDB instance 
     * 
     * Default is localhost
     * 
     * @param host The host to set
     * @return EmbeddedMongoDB instance 
     */
    public EmbeddedMongoDB withHost(String host) {
        Objects.requireNonNull(host, "host can not be null");
        
        this.host = host;
        return this;
    }
    
    /**
     * Sets the version for the EmbeddedMongoDB instance 
     * 
     * Default is Version.Main.PRODUCTION
     * 
     * @param version The version to set
     * @return EmbeddedMongoDB instance 
     */
    public EmbeddedMongoDB withVersion(Version.Main version) {
        Objects.requireNonNull(version, "version can not be null");
        
        this.version = version;
        return this;
    }

    /**
     * Starts the EmbeddedMongoDB instance
     * @return EmbeddedMongoDB instance 
     */
    public EmbeddedMongoDB start() {
        if (!active && !inUse(host, port)) {
            try {
                Command command = Command.MongoD;
                RuntimeConfig runtimeConfig = Defaults.runtimeConfigFor(command)
                        .processOutput(ProcessOutput.getDefaultInstanceSilent())
                        .build();
                
                mongodProcess = MongodStarter.getInstance(runtimeConfig).prepare(MongodConfig.builder()
                        .version(version)
                        .net(new Net(host, port, false))
                        .build())
                        .start();

                active = true;
                log.info("Successfully started EmbeddedMongoDB @ {}:{}", host, port);
            } catch (IOException e) {
                log.error("Failed to start EmbeddedMongoDB @ {}:{}", host, port, e);
            }
        }
        
        return this;
    }
    
    
    /**
     * Checks if the given host and port is already in use
     * 
     * @param host The host to check
     * @param port The port to check
     * @return True is port is in use, false otherwise
     */
    private boolean inUse(String host, int port) {
        boolean result = false;

        try {
            (new Socket(host, port)).close();
            result = true;
        } catch (IOException e) {
            log.warn("Did not (re-)start EmbeddedMongoDB @ {}:{} - looks like port is already in use?!", this.host, this.port, e);
        }

        return result;
    }
    
    /**
     * Stops the EmbeddedMongoDB instance
     */
    public void stop() {
        if (active) {
            mongodProcess.stop();
            active = false;

            log.info("Successfully stopped EmbeddedMongoDB @ {}:{}", host, port);
        }
    }

    public String getHost() {
        return host;
    }

    public Version.Main getVersion() {
        return version;
    }

    public int getPort() {
        return port;
    }

    public boolean isActive() {
        return active;
    }

}
