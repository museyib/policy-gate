package llc.devlab.app.security.service;

import llc.devlab.app.exception.ResourceNotFoundException;
import llc.devlab.app.security.mapper.RoleMapper;
import llc.devlab.app.security.model.dto.RoleDto;
import llc.devlab.app.security.model.entity.AppRole;
import llc.devlab.app.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .toList();
    }

    public RoleDto getRoleById(Long id) {
        return roleMapper.toDto(loadAppRole(id));
    }

    @Transactional
    public RoleDto createRole(RoleDto roleDto) {
        return roleMapper.toDto(roleRepository.save(roleMapper.toEntity(roleDto)));
    }

    @Transactional
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        AppRole role = loadAppRole(id);
        roleMapper.updateEntity(roleDto, role);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Transactional
    public void deleteRole(Long id) {
        AppRole role = loadAppRole(id);
        roleRepository.delete(role);
    }

    private AppRole loadAppRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }
}
