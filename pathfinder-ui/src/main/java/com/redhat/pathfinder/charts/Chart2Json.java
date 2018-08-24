package com.redhat.pathfinder.charts;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Chart2Json {
  private Set<String> labels=new LinkedHashSet<String>();
  private List<DataSet2> datasets=new ArrayList<DataSet2>();
  
  public Set<String> getLabels() {
    return labels;
  }
  public void setLabels(Set<String> labels) {
    this.labels=labels;
  }
  public List<DataSet2> getDatasets() {
    return datasets;
  }
  public void setDatasets(List<DataSet2> datasets) {
    this.datasets=datasets;
  }
  
  
}
