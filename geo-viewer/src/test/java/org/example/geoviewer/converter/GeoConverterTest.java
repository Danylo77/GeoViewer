package org.example.geoviewer.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoConverterTest {

    private GeoConverter converter;

    @BeforeEach
    void setUp() {
        converter = new GeoConverter();
    }

    @Test
    void convertPoint() {
        String result = converter.wktToGeoJson("POINT (30 10)");
        assertEquals("{\"type\":\"Point\",\"coordinates\":[30,10]}", result);
    }

    @Test
    void convertPointWithDecimals() {
        String result = converter.wktToGeoJson("POINT (30.5 10.2)");
        assertEquals("{\"type\":\"Point\",\"coordinates\":[30.5,10.2]}", result);
    }

    @Test
    void convertLineString() {
        String result = converter.wktToGeoJson("LINESTRING (30 10, 10 30, 40 40)");
        assertEquals("{\"type\":\"LineString\",\"coordinates\":[[30,10],[10,30],[40,40]]}", result);
    }

    @Test
    void nullInputThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> converter.wktToGeoJson(null));
    }

    @Test
    void emptyInputThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> converter.wktToGeoJson(""));
    }

    @Test
    void unsupportedTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> converter.wktToGeoJson("MULTIPOINT ((10 40))"));
    }

    @Test
    void convertPointCaseInsensitive() {
        String result = converter.wktToGeoJson("point (5 15)");
        assertEquals("{\"type\":\"Point\",\"coordinates\":[5,15]}", result);
    }
}

