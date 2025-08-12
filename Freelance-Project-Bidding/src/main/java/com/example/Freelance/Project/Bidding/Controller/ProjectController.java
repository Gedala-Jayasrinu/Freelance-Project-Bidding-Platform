package com.example.Freelance.Project.Bidding.Controller;

import com.example.Freelance.Project.Bidding.Entity.Project;
import com.example.Freelance.Project.Bidding.Entity.User;
import com.example.Freelance.Project.Bidding.Service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/projects")
@PreAuthorize("hasRole('CLIENT')")

public class ProjectController {
    @Autowired
    private  ProjectService projectService;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        User client = getCurrentUser();
        return ResponseEntity.ok(projectService.createProject(project, client));
    }

    @GetMapping
    public ResponseEntity<List<Project>> getClientProjects() {
        return ResponseEntity.ok(projectService.getClientProjects(getCurrentUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long id,
            @RequestBody Project updatedProject
    ) {
        return ResponseEntity.ok(projectService.updateProject(id, updatedProject, getCurrentUser()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id, getCurrentUser());
        return ResponseEntity.noContent().build();
    }
}
