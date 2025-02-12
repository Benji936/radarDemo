package demo.rad.ar.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import demo.rad.ar.main.services.UserSegmentationService;

@RestController
@RequestMapping("/api/segmentation")
public class SegmentationController {

    @Autowired
    private UserSegmentationService segmentationService;

    @PostMapping("/run")
    public String runSegmentation(@RequestParam List<String> attributes, @RequestParam(defaultValue = "3") int clusters) {
        segmentationService.performSegmentation(attributes, clusters);
        return "User segmentation completed with " + clusters + " clusters using attributes: " + attributes;
    }
}

