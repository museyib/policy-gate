package llc.devlab.app.security.mapper;

import llc.devlab.app.security.model.dto.ResourceCreateRequest;
import llc.devlab.app.security.model.dto.ResourceDto;
import llc.devlab.app.security.model.dto.ResourceUpdateRequest;
import llc.devlab.app.security.model.entity.Resource;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    ResourceDto toDto(Resource resource);

    Resource toEntity(ResourceCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ResourceUpdateRequest request, @MappingTarget Resource resource);
}
