package org.example.geoviewer.dto;

public class ConvertRequest {
    private String format;
    private String data;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}