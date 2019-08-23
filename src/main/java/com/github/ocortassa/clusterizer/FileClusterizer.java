package com.github.ocortassa.clusterizer;

import com.github.ocortassa.clusterizer.cli.Command;
import com.github.ocortassa.clusterizer.model.Cluster;
import com.github.ocortassa.clusterizer.model.ClusterElement;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * File Clusterizer by ...
 * @author [OC]
 */
public class FileClusterizer {

	private final Logger LOGGER = LogManager.getLogger(FileClusterizer.class);

	private List<String> files = new ArrayList<>();
	private Map<String, Cluster> clusters = new HashMap<>();

	public void clusterByExtension(Command command) {
	    loadItemsToProcess(command.getBaseDir());
        List<ClusterElement> elements = new ArrayList<>();
        for (String file : files) {
            elements.add(loadClusterElements(file, command.getBaseDir()));
        }

        // Clustering by Extension
        for (ClusterElement element : elements) {
            String clusterKey = element.getExtension().trim().toLowerCase();
            Cluster cluster = clusters.get(clusterKey);
            if (cluster == null) {
                // Found new cluster, add it
                Cluster newCluster = new Cluster();
                clusters.put(clusterKey, newCluster);
                newCluster.addElement(element);
            } else {
                // Found already created, add an item
                cluster.addElement(element);
            }
        }

        // Apply cluster's elements moving
        if ( !command.isDryRun() ) {
            // TODO: file moving according to the clustering criteria
        } else {
            for (Map.Entry<String, Cluster> cluster : clusters.entrySet()) {
                for (ClusterElement element : cluster.getValue().getElements()) {
                    LOGGER.info("[{} -> {}]", cluster.getKey(), element.getFileAbsolutePath() + "/" + element.getFileName());
                }
            }
        }

    }

    private void loadItemsToProcess(String baseDir) {
        try (Stream<Path> walk = Files.walk(Paths.get(baseDir))) {

            List<String> items = walk
                    .filter(Files::isRegularFile)
                    .map(
                            x -> x.toString()).collect(Collectors.toList()
                    );

            items.forEach(LOGGER::debug);
            files = items;

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private ClusterElement loadClusterElements(String filePath, String basePath) {
        ClusterElement clusterElement = new ClusterElement();
        clusterElement.setFileAbsolutePath(filePath);
        clusterElement.setFileRelativePath( filePath.substring(filePath.indexOf(basePath) + basePath.length() + 1) );
        clusterElement.setFileName( FilenameUtils.getName(filePath) );
        clusterElement.setExtension( FilenameUtils.getExtension(filePath) );
        clusterElement.setSize( FileUtils.sizeOf( new File(filePath) ) );
        return clusterElement;
	}

}
