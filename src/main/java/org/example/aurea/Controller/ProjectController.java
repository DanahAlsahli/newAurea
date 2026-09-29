package org.example.aurea.Controller;

import org.example.aurea.Model.Project;
import org.example.aurea.Service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProject(
            @RequestBody Project project) {

        return ResponseEntity
                .status(201)
                .body(projectService.addProject(project));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllProjects() {

        return ResponseEntity
                .status(200)
                .body(projectService.getAllProjects());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getProjectById(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getProjectById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProject(
            @PathVariable Integer id,
            @RequestBody Project project) {

        projectService.updateProject(id, project);

        return ResponseEntity
                .status(200)
                .body("Project updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProject(
            @PathVariable Integer id) {

        projectService.deleteProject(id);

        return ResponseEntity
                .status(200)
                .body("Project deleted successfully");
    }

    @GetMapping("/dashboard/{id}")
    public ResponseEntity<?> getProjectDashboard(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getProjectDashboard(id));
    }


    @GetMapping("/health/{id}")
    public ResponseEntity<?> getProjectHealth(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getProjectHealth(id));
    }

    @GetMapping("/insights/{id}")
    public ResponseEntity<?> getProjectInsights(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getProjectInsights(id));
    }

    @GetMapping("/risks/{id}")
    public ResponseEntity<?> getProjectRisks(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getProjectRisks(id));
    }

    @GetMapping("/opportunities/{id}")
    public ResponseEntity<?> getProjectOpportunities(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getProjectOpportunities(id));
    }

    @GetMapping("/recommendations/{id}")
    public ResponseEntity<?> getProjectRecommendations(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getProjectRecommendations(id));
    }

    @GetMapping("/performance/{id}")
    public ResponseEntity<?> getProjectPerformance(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getProjectPerformance(id));
    }

    @GetMapping("/forecast/{id}")
    public ResponseEntity<?> getProjectForecast(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getProjectForecast(id));
    }

    @GetMapping("/early-warnings/{id}")
    public ResponseEntity<?> getEarlyWarnings(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getEarlyWarnings(id));
    }

    @GetMapping("/executive-brief/{id}")
    public ResponseEntity<?> getExecutiveBrief(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getExecutiveBrief(id));
    }

    @GetMapping("/decision-memory/{id}")
    public ResponseEntity<?> getDecisionMemory(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getDecisionMemory(id));
    }

    @GetMapping("/intelligence/{id}")
    public ResponseEntity<?> getProjectIntelligence(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getProjectIntelligence(id));
    }

    @GetMapping("/executive-insight/{id}")
    public ResponseEntity<?> getExecutiveInsight(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(projectService.getExecutiveInsight(id));
    }

}


