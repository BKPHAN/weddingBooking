package com.demo.web.mapper.admin;

import com.demo.web.dto.admin.PromotionRequest;
import com.demo.web.dto.admin.PromotionResponse;
import com.demo.web.model.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PromotionAdminMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    Promotion toEntity(PromotionRequest request);

    PromotionResponse toResponse(Promotion promotion);

    List<PromotionResponse> toResponseList(List<Promotion> promotions);
}
