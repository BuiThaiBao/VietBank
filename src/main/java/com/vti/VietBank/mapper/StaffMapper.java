package com.vti.VietBank.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.vti.VietBank.dto.request.staff.CreateStaffRequest;
import com.vti.VietBank.dto.response.staff.StaffResponse;
import com.vti.VietBank.entity.Staff;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StaffMapper {
    StaffResponse toResponse(Staff staff);

    Staff toEntity(CreateStaffRequest request);
}
