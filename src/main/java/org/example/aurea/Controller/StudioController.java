package org.example.aurea.Controller;

import org.example.aurea.Model.Studio;
import org.example.aurea.Service.StudioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/studio")
public class StudioController {

    private final StudioService studioService;

    public StudioController(StudioService studioService) {
        this.studioService = studioService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStudio(
            @RequestBody Studio studio) {

        return ResponseEntity
                .status(201)
                .body(
                        studioService.addStudio(studio)
                );
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllStudios() {

        return ResponseEntity
                .status(200)
                .body(
                        studioService.getAllStudios()
                );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getStudioById(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(
                        studioService.getStudioById(id)
                );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudio(
            @PathVariable Integer id,
            @RequestBody Studio studio) {

        studioService.updateStudio(
                id,
                studio
        );

        return ResponseEntity
                .status(200)
                .body(
                        "Studio updated successfully"
                );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudio(
            @PathVariable Integer id) {

        studioService.deleteStudio(id);

        return ResponseEntity
                .status(200)
                .body(
                        "Studio deleted successfully"
                );
    }
}


