package org.example.geoviewer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.geoviewer.dto.ConvertRequest;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.Map;

@Service
public class GeoService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Object convert(ConvertRequest request) throws Exception {

        String format = request.getFormat().toLowerCase();
        String data = request.getData();

        switch (format) {

            case "geojson":
                // просто перевіряємо що це валідний JSON
                return objectMapper.readValue(data, Map.class);

            case "wkt":
                return convertWKT(data);

            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }

    private Object convertWKT(String wkt) throws Exception {
        WKTReader reader = new WKTReader();
        Geometry geometry = reader.read(wkt);

        GeoJSONWriter writer = new GeoJSONWriter();
        return writer.write(geometry);
    }
}