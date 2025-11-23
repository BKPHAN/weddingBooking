package com.demo.web.mapper.admin;

import com.demo.web.dto.admin.MediaItemRequest;
import com.demo.web.dto.admin.MediaItemResponse;
import com.demo.web.model.MediaItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaItemAdminMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "album", ignore = true)
    MediaItem toEntity(MediaItemRequest request);

    MediaItemResponse toResponse(MediaItem item);

    List<MediaItemResponse> toResponseList(List<MediaItem> items);
}
