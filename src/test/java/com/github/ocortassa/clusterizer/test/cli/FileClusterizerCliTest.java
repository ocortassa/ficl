package com.github.ocortassa.clusterizer.test.cli;

import com.github.ocortassa.clusterizer.cli.FileClusterizerCli;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class FileClusterizerCliTest {

    private final Logger LOGGER = LogManager.getLogger(FileClusterizerCliTest.class);

    @Test
    public void doClusterizeByExtensionTest() {
        try {

            String[] args = {
                    "--baseDir", "C:\\Users\\Omar\\Desktop\\ebooks\\manuali-da-riorganizzare",
                    "--clusterBy", "extension",
                    "--dryRun", "Y"
            };
            FileClusterizerCli.main(args);
            Assert.assertTrue(true);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void doClusterizeByMetadataDateTest() {
        try {

            String[] args = {
                    //"--baseDir", "C:\\Users\\Omar\\Desktop\\Foto 2011-11-25 10;22;42\\Foto\\2010-06 - Roma",
                    "--baseDir", "C:\\Users\\Omar\\Desktop\\Foto 2011-11-25 10;22;42\\Foto",
                    "--clusterBy", "metadataDate",
                    //"--dryRun", "Y"
                    "--dryRun", "N"
            };
            FileClusterizerCli.main(args);
            Assert.assertTrue(true);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            Assert.fail(e.getMessage());
        }
    }

}
