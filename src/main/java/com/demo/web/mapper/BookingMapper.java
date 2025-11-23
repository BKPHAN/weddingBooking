package com.demo.web.mapper;

import com.demo.web.dto.BookingRequest;
import com.demo.web.dto.BookingResponse;
import com.demo.web.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "hall", ignore = true),
            @Mapping(target = "services", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "flag", ignore = true)
    })
    Booking toEntity(BookingRequest request);

    @Mapping(target = "hallName", expression = "java(booking.getHall() != null ? booking.getHall().getName() : null)")
    @Mapping(target = "status", source = "status")
    BookingResponse toResponse(Booking booking);
}
