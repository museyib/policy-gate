package llc.devlab.app.security.mapper;

import llc.devlab.app.security.model.dto.RoleDto;
import llc.devlab.app.security.model.entity.AppRole;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto toDto(AppRole role);

    AppRole toEntity(RoleDto roleDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(RoleDto roleDto, @MappingTarget AppRole role);
}
