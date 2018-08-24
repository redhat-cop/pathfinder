package com.redhat.pathfinder.charts;

import java.util.ArrayList;
import java.util.List;

public class DataSet2 {
//  private String fillColor; //ie "rgba(220,220,220,0.2)"
//  private String strokeColor; //ie "rgba(220,220,220,1)"
  
  private List<Integer> data;
  private Integer borderWidth;
  private Integer hoverBorderWidth;  
  private List<String> backgroundColor;
  private List<String> borderColor;
  private String label;
  
  public String getLabel() {
    return label;
  }
  public void setLabel(String label) {
    this.label = label;
  }
  //  public String getFillColor() {
//    return fillColor;
//  }
//  public void setFillColor(String fillColor) {
//    this.fillColor=fillColor;
//  }
//  public String getStrokeColor() {
//    return strokeColor;
//  }
//  public void setStrokeColor(String strokeColor) {
//    this.strokeColor=strokeColor;
//  }
  public List<Integer> getData() {
    if (data==null) data=new ArrayList<Integer>();
    return data;
  }
  public void setData(List<Integer> data) {
    this.data=data;
  }
  public Integer getBorderWidth() {
    return borderWidth;
  }
  public void setBorderWidth(Integer borderWidth) {
    this.borderWidth = borderWidth;
  }
  public List<String> getBackgroundColor() {
    if (backgroundColor==null) backgroundColor=new ArrayList<String>();
    return backgroundColor;
  }
  public void setBackgroundColor(List<String> backgroundColor) {
    this.backgroundColor = backgroundColor;
  }
   public void setHoverBorderWidth(Integer hoverBorderWidth) {
    this.hoverBorderWidth = hoverBorderWidth;
  }
  public List<String> getBorderColor() {
    if (borderColor==null) borderColor=new ArrayList<String>();
    return borderColor;
  }
  public void setBorderColor(List<String> borderColor) {
    this.borderColor = borderColor;
  }
  
  
}
