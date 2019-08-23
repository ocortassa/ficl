package com.github.ocortassa.clusterizer.model;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private List<ClusterElement> elements = new ArrayList<>();

    public List<ClusterElement> getElements() {
        return elements;
    }

    public int getClusterSize() {
        return elements.size();
    }

    public void addElement(ClusterElement element) {
        elements.add(element);
    }

}
