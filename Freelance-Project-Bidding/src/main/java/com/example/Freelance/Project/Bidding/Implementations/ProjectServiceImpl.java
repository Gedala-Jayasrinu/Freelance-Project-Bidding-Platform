package com.example.Freelance.Project.Bidding.Implementations;

import com.example.Freelance.Project.Bidding.Entity.Project;
import com.example.Freelance.Project.Bidding.Entity.User;

import com.example.Freelance.Project.Bidding.ExceptionHandling.ProjectNotFoundException;
import com.example.Freelance.Project.Bidding.ExceptionHandling.UnauthorizedProjectAccessException;
import com.example.Freelance.Project.Bidding.Repository.ProjectRepository;
import com.example.Freelance.Project.Bidding.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project createProject(Project project, User client) {
        project.setClient(client);
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getClientProjects(User client) {
        return projectRepository.findByClientId(client.getId());
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
    }

    @Override
    public Project updateProject(Long id, Project updated, User client) {
        Project project = getProjectById(id);
        if (!project.getClient().getId().equals(client.getId())) {
            throw new UnauthorizedProjectAccessException("You are not authorized to update this project");
        }
        project.setTitle(updated.getTitle());
        project.setDescription(updated.getDescription());
        project.setBudget(updated.getBudget());
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id, User client) {
        Project project = getProjectById(id);
        if (!project.getClient().getId().equals(client.getId())) {
            throw new UnauthorizedProjectAccessException("You are not authorized to delete this project");
        }
        projectRepository.delete(project);
    }
}
