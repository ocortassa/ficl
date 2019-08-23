package com.github.ocortassa.clusterizer.cli;

public class Command {
    private String baseDir;
    private String clusterCriteria;
    private String dryRun = "N";

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getClusterCriteria() {
        return clusterCriteria;
    }

    public void setClusterCriteria(String clusterCriteria) {
        this.clusterCriteria = clusterCriteria;
    }

    public boolean isDryRun() {
        return "YS".contains(dryRun.toUpperCase());
    }

    public void setDryRun(String dryRun) {
        this.dryRun = dryRun;
    }

    @Override
    public String toString() {
        return "Command{" +
                "baseDir='" + baseDir + '\'' +
                ", clusterCriteria='" + clusterCriteria + '\'' +
                ", dryRun=" + dryRun +
                '}';
    }
}
