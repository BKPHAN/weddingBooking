package com.demo.web.mapper.admin;

import com.demo.web.dto.admin.MenuRequest;
import com.demo.web.dto.admin.MenuResponse;
import com.demo.web.model.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuAdminMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    Menu toEntity(MenuRequest request);

    MenuResponse toResponse(Menu menu);

    List<MenuResponse> toResponseList(List<Menu> menus);
}
