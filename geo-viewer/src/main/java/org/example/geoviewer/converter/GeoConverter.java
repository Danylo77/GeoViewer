package org.example.geoviewer.converter;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for converting WKT (Well-Known Text) to GeoJSON format
 * without using external libraries.
 */
public class GeoConverter {

    public String wktToGeoJson(String wkt) {
        if (wkt == null || wkt.trim().isEmpty()) {
            throw new IllegalArgumentException("WKT string cannot be null or empty");
        }

        wkt = wkt.trim();

        if (wkt.toUpperCase().startsWith("POINT")) {
            return convertPoint(wkt);
        } else if (wkt.toUpperCase().startsWith("LINESTRING")) {
            return convertLineString(wkt);
        } else if (wkt.toUpperCase().startsWith("POLYGON")) {
            return convertPolygon(wkt);
        } else {
            throw new IllegalArgumentException("Unsupported WKT geometry type: " + wkt);
        }
    }

    private String convertPoint(String wkt) {
        String coords = extractCoordinateBlock(wkt, "POINT");
        double[] point = parsePoint(coords.trim());
        return "{\"type\":\"Point\",\"coordinates\":[" + formatCoord(point) + "]}";
    }

    private String convertLineString(String wkt) {
        String coords = extractCoordinateBlock(wkt, "LINESTRING");
        List<double[]> points = parsePointList(coords);
        return "{\"type\":\"LineString\",\"coordinates\":[" + formatPointList(points) + "]}";
    }

    private String convertPolygon(String wkt) {
        String block = extractCoordinateBlock(wkt, "POLYGON");
        // Remove outer parentheses for polygon rings
        block = block.trim();
        if (block.startsWith("(") && block.endsWith(")")) {
            block = block.substring(1, block.length() - 1);
        }

        List<List<double[]>> rings = new ArrayList<>();
        for (String ringStr : splitRings(block)) {
            rings.add(parsePointList(ringStr.trim()));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{\"type\":\"Polygon\",\"coordinates\":[");
        for (int i = 0; i < rings.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("[").append(formatPointList(rings.get(i))).append("]");
        }
        sb.append("]}");
        return sb.toString();
    }

    String extractCoordinateBlock(String wkt, String type) {
        int start = wkt.toUpperCase().indexOf(type.toUpperCase()) + type.length();
        String rest = wkt.substring(start).trim();
        if (rest.startsWith("(") && rest.endsWith(")")) {
            return rest.substring(1, rest.length() - 1);
        }
        throw new IllegalArgumentException("Invalid WKT format: " + wkt);
    }

    double[] parsePoint(String s) {
        String[] parts = s.trim().split("\\s+");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid point: " + s);
        }
        return new double[]{Double.parseDouble(parts[0]), Double.parseDouble(parts[1])};
    }

    List<double[]> parsePointList(String s) {
        String[] pointStrs = s.split(",");
        List<double[]> points = new ArrayList<>();
        for (String p : pointStrs) {
            points.add(parsePoint(p.trim()));
        }
        return points;
    }

    private List<String> splitRings(String block) {
        List<String> rings = new ArrayList<>();
        int depth = 0;
        StringBuilder current = new StringBuilder();
        for (char c : block.toCharArray()) {
            if (c == '(') {
                depth++;
                if (depth == 1) continue;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    rings.add(current.toString());
                    current = new StringBuilder();
                    continue;
                }
            } else if (c == ',' && depth == 0) {
                continue;
            }
            if (depth >= 1) {
                current.append(c);
            }
        }
        return rings;
    }

    private String formatCoord(double[] point) {
        return formatDouble(point[0]) + "," + formatDouble(point[1]);
    }

    private String formatPointList(List<double[]> points) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < points.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("[").append(formatCoord(points.get(i))).append("]");
        }
        return sb.toString();
    }

    private String formatDouble(double d) {
        if (d == (long) d) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }
}

