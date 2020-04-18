package com.github.ocortassa.clusterizer;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	public void clusterByExtension(Command command) throws IOException {
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

            // Create output directory, if it doesn't exists
            String outDirectory = command.getBaseDir() + "/out";
            File outDirectoryFile = new File(outDirectory);
            if ( !outDirectoryFile.exists() ) {
                FileUtils.forceMkdir(outDirectoryFile);
            }

            // Cluster processing
            for (Map.Entry<String, Cluster> cluster : clusters.entrySet()) {
                for (ClusterElement element : cluster.getValue().getElements()) {
                    LOGGER.info(" -- [{} -> {}] --", cluster.getKey(), element.getFullFilePath());

                    // Create extension directory, if it doesn't exists
                    String extDirectory = outDirectory + "/" + cluster.getKey();
                    File extDirectoryFile = new File(extDirectory);
                    if ( !extDirectoryFile.exists() ) {
                        FileUtils.forceMkdir(extDirectoryFile);
                    }

                    String newClusterElementPath = extDirectoryFile + "/" + element.getFileName();
                    File newClusterElementPathFile = new File(newClusterElementPath);
                    if ( !newClusterElementPathFile.exists() ) {
                        FileUtils.copyFile(new File(element.getFileAbsolutePath()), newClusterElementPathFile);
                    }

                }
            }

        } else {
            long counter = 0;
            for (Map.Entry<String, Cluster> cluster : clusters.entrySet()) {
                for (ClusterElement element : cluster.getValue().getElements()) {
                    LOGGER.info("[{} -> {}]", cluster.getKey(), element.getFileAbsolutePath());
                    counter++;
                }
            }
            LOGGER.info("Founded {} items", counter);
        }

    }

    public void clusterByMetadataDate(Command command) throws IOException {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            loadItemsToProcess(command.getBaseDir());
            List<ClusterElement> elements = new ArrayList<>();
            for (String file : files) {
                elements.add(loadClusterElements(file, command.getBaseDir()));
            }
            // Clustering by Extension
            for (ClusterElement element : elements) {

                if ("jpg|png".contains(element.getExtension().toLowerCase())) {
                    Metadata metadata = ImageMetadataReader.readMetadata( new File(element.getFileAbsolutePath()) );
                    /*
                    for (Directory directory : metadata.getDirectories()) {
                        System.out.println("\t" + file + " >>> " + directory.getName());
                        for (Tag tag : directory.getTags()) {
                            System.out.println("\t\t" + tag.getTagName() + " -> " + tag.getDescription());
                        }
                    }
                    */
                    ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                    if (directory != null) {
                        Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                        if (date != null) {
                            String dateTimeOriginal = simpleDateFormat.format(date);
                            System.out.println(element.getFileName() + " >>> " + dateTimeOriginal);

                            // Create cluster
                            String clusterKey = dateTimeOriginal.substring(0, 7);
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
                        } else {
                            LOGGER.info(element.getFileAbsolutePath() + " >> date: " + date);
                        }
                    } else {
                        LOGGER.info(element.getFileAbsolutePath() + " >> directory: " + directory);
                    }

                } else {
                    System.out.println(element.getFileName() + " >>> SCARTATO");
                }
            }   // end fort

            // Apply cluster's elements moving
            if ( !command.isDryRun() ) {

                // Create output directory, if it doesn't exists
                String outDirectory = command.getBaseDir() + "/out";
                File outDirectoryFile = new File(outDirectory);
                if ( !outDirectoryFile.exists() ) {
                    FileUtils.forceMkdir(outDirectoryFile);
                }

                // Cluster processing
                for (Map.Entry<String, Cluster> cluster : clusters.entrySet()) {
                    for (ClusterElement element : cluster.getValue().getElements()) {
                        LOGGER.info(" -- [{} -> {}] --", cluster.getKey(), element.getFullFilePath());

                        // Create extension directory, if it doesn't exists
                        String extDirectory = outDirectory + "/" + cluster.getKey();
                        File extDirectoryFile = new File(extDirectory);
                        if ( !extDirectoryFile.exists() ) {
                            FileUtils.forceMkdir(extDirectoryFile);
                        }

                        String newClusterElementPath = extDirectoryFile + "/" + element.getFileName();
                        File newClusterElementPathFile = new File(newClusterElementPath);
                        if ( !newClusterElementPathFile.exists() ) {
                            FileUtils.copyFile(new File(element.getFileAbsolutePath()), newClusterElementPathFile);
                        }
                    }
                }

            } else {
                long counter = 0;
                for (Map.Entry<String, Cluster> cluster : clusters.entrySet()) {
                    for (ClusterElement element : cluster.getValue().getElements()) {
                        LOGGER.info("[{} -> {}]", cluster.getKey(), element.getFileAbsolutePath());
                        counter++;
                    }
                }
                LOGGER.info("Founded {} items", counter);
            }

            } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new IOException(e.getMessage(), e);
        }

    }

    private void loadItemsToProcess(String baseDir) {
        try (Stream<Path> walk = Files.walk(Paths.get(baseDir))) {

            List<String> items = walk
                    .filter(Files::isRegularFile)
                    .map(
                            x -> x.toString()).collect(Collectors.toList()
                    );

            items.forEach(LOGGER::trace);
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
