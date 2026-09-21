package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.PropertyDTO;
import com.petproject.term_paper.dto.ViewingRequestDTO;
import com.petproject.term_paper.dto.mapping.PropertyMapping;
import com.petproject.term_paper.service.PropertyService;
import com.petproject.term_paper.service.ViewingRequestService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/agent")
@AllArgsConstructor
@PreAuthorize("hasRole('AGENT')")
public class AgentController {
    private final PropertyService properties;
    private final ViewingRequestService requests;
    private final PropertyMapping mapping;

    @GetMapping("/properties")
    public List<PropertyDTO> properties(Principal principal) {
        return properties.getAssignedProperties(principal.getName()).stream().map(mapping::toDTO).toList();
    }

    @GetMapping("/viewing-requests")
    public List<ViewingRequestDTO> requests(Principal principal) {
        return requests.getAssignedToAgent(principal.getName());
    }
}
