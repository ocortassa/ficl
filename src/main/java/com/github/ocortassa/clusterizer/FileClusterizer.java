package com.github.ocortassa.clusterizer;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * File Clusterizer by ...
 * @author [OC]
 */
public class FileClusterizer {

	private final Logger LOGGER = Logger.getLogger(FileClusterizer.class);

	public void clusterize() {
        try (Stream<Path> walk = Files.walk(Paths.get("C:\\Users\\Omar\\Desktop\\ebooks\\manuali-da-riorganizzare"))) {

            List<String> result = walk
                    .filter(Files::isRegularFile)
                    .map(
                            x -> x.toString()).collect(Collectors.toList()
                    );

            result.forEach(System.out::println);

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        FileClusterizer ficl = new FileClusterizer();
        ficl.clusterize();
    }
}
