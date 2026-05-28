package org.example.geoviewer.controller;

import org.example.geoviewer.dto.ConvertRequest;
import org.example.geoviewer.service.GeoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/convert")
@CrossOrigin
public class GeoController {

    private final GeoService geoService;

    public GeoController(GeoService geoService) {
        this.geoService = geoService;
    }

    @PostMapping
    public Object convert(@RequestBody ConvertRequest request) throws Exception {
        return geoService.convert(request);
    }
}