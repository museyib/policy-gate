package llc.devlab.app.security.service;

import llc.devlab.app.exception.ResourceNotFoundException;
import llc.devlab.app.security.mapper.ResourceMapper;
import llc.devlab.app.security.model.dto.ResourceCreateRequest;
import llc.devlab.app.security.model.dto.ResourceDto;
import llc.devlab.app.security.model.dto.ResourceUpdateRequest;
import llc.devlab.app.security.model.entity.Resource;
import llc.devlab.app.security.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    public List<ResourceDto> getResources() {
        return resourceRepository.findAll().stream().map(resourceMapper::toDto).toList();
    }

    public ResourceDto getResourceById(Long id) {
        return resourceMapper.toDto(loadResourceById(id));
    }

    @Transactional
    public ResourceDto createResource(ResourceCreateRequest request) {
        return resourceMapper.toDto(resourceRepository.save(resourceMapper.toEntity(request)));
    }

    @Transactional
    public ResourceDto updateResource(Long id, ResourceUpdateRequest request) {
        Resource resource = loadResourceById(id);
        resourceMapper.updateEntity(request, resource);
        return resourceMapper.toDto(resourceRepository.save(resource));
    }

    @Transactional
    public void deleteResource(Long id) {
        Resource resource = loadResourceById(id);
        resourceRepository.delete(resource);
    }

    public Resource loadResourceById(Long id) {
        return resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }
}
