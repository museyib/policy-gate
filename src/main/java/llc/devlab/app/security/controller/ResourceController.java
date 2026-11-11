package llc.devlab.app.security.controller;

import llc.devlab.app.security.model.dto.ResourceCreateRequest;
import llc.devlab.app.security.model.dto.ResourceDto;
import llc.devlab.app.security.model.dto.ResourceUpdateRequest;
import llc.devlab.app.security.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping
    @PreAuthorize("hasPermission('RESOURCE', 'READ')")
    public ResponseEntity<List<ResourceDto>> getResources() {
        return ResponseEntity.ok(resourceService.getResources());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('RESOURCE', 'READ')")
    public ResponseEntity<ResourceDto> getResource(@PathVariable Long id) {
        return ResponseEntity.ok(resourceService.getResourceById(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission('RESOURCE', 'CREATE')")
    public ResponseEntity<ResourceDto> createResource(@RequestBody ResourceCreateRequest request) {
        return ResponseEntity.ok(resourceService.createResource(request));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasPermission('RESOURCE', 'UPDATE')")
    public ResponseEntity<ResourceDto> updateResource(@PathVariable Long id, @RequestBody ResourceUpdateRequest request) {
        return ResponseEntity.ok(resourceService.updateResource(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('RESOURCE', 'DELETE')")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}
