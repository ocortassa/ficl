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
                    "--baseDir", "C:\\Temp",
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

}
