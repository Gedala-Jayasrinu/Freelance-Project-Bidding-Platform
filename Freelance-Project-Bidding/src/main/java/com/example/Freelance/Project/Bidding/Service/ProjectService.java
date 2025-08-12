package com.example.Freelance.Project.Bidding.Service;

import com.example.Freelance.Project.Bidding.Entity.Project;
import com.example.Freelance.Project.Bidding.Entity.User;

import java.util.List;

public interface ProjectService {
    Project createProject(Project project, User client);
    List<Project> getClientProjects(User client);
    Project getProjectById(Long id);
    Project updateProject(Long id, Project updatedProject, User client);
    void deleteProject(Long id, User client);
}
