package com.github.ocortassa.clusterizer.cli;

import com.github.ocortassa.clusterizer.FileClusterizer;
import io.leego.banana.BananaUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * File Clusterizer Command Line Interface
 */
@CommandLine.Command(name = "FIle CLusterizer", mixinStandardHelpOptions = true, header = "%n@|green FIle CLusterizer|@")
public class FileClusterizerCli implements Callable<Void> {

    private final static Logger LOGGER = LogManager.getLogger(FileClusterizerCli.class);

    @CommandLine.Option(names = {"-bd", "--baseDir"}, arity = "1", description = "The base directory to process", required = true)
    private String baseDir;

    @CommandLine.Option(names = {"-c", "--clusterBy"}, arity = "1", description = "The clustering criteria", required = true)
    private String clusterCriteria;

    @CommandLine.Option(names = {"--dryRun"}, arity = "0", description = "The dry run flag (simulate the cation without modify any file)")
    private String dryRun = "N";

    @Override
    public Void call() {
        try {

            LOGGER.info( "\n" + BananaUtils.bananaify("FIle CLusterizer") );

            Command command = new Command();
            command.setBaseDir(baseDir);
            command.setClusterCriteria(clusterCriteria);
            command.setDryRun(dryRun);

            FileClusterizer clusterizer = new FileClusterizer();
            if ("extension".equalsIgnoreCase(clusterCriteria)) {
                LOGGER.info("Clusterize by Extension");
                clusterizer.clusterByExtension(command);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(1);
        }
        return null;
    }

    public static void main(String... args) {
        CommandLine.call(new FileClusterizerCli(), args);
    }


}
