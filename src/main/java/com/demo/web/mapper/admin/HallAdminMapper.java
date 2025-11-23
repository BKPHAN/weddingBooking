package com.demo.web.mapper.admin;

import com.demo.web.dto.admin.HallRequest;
import com.demo.web.dto.admin.HallResponse;
import com.demo.web.model.Hall;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HallAdminMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    Hall toEntity(HallRequest request);

    HallResponse toResponse(Hall hall);

    List<HallResponse> toResponseList(List<Hall> halls);
}
