package com.github.ocortassa.clusterizer.model;

public class ClusterElement {
    private String fileAbsolutePath;
    private String fileRelativePath;
    private String fileName;
    private String extension;
    private long size;

    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public void setFileAbsolutePath(String fileAbsolutePath) {
        this.fileAbsolutePath = fileAbsolutePath;
    }

    public String getFileRelativePath() {
        return fileRelativePath;
    }

    public void setFileRelativePath(String fileRelativePath) {
        this.fileRelativePath = fileRelativePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ClusterItem{" +
                "fileAbsolutePath='" + fileAbsolutePath + '\'' +
                ", fileRelativePath='" + fileRelativePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", extension='" + extension + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

}
