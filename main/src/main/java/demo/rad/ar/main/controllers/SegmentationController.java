package demo.rad.ar.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import demo.rad.ar.main.services.UserSegmentationService;

@RestController
@RequestMapping("/api/segmentation")
public class SegmentationController {

    @Autowired
    private UserSegmentationService segmentationService;

    @PostMapping("/run")
    public String runSegmentation() {
        segmentationService.performSegmentation();
        return "User segmentation completed!";
    }
}

