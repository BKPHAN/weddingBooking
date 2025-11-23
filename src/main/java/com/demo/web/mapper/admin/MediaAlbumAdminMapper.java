package com.demo.web.mapper.admin;

import com.demo.web.dto.admin.MediaAlbumRequest;
import com.demo.web.dto.admin.MediaAlbumResponse;
import com.demo.web.model.MediaAlbum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaAlbumAdminMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MediaAlbum toEntity(MediaAlbumRequest request);

    MediaAlbumResponse toResponse(MediaAlbum album);

    List<MediaAlbumResponse> toResponseList(List<MediaAlbum> albums);
}
