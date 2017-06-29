package com.gk.service.mapper;

import com.gk.domain.*;
import com.gk.service.dto.StudentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Student and its DTO StudentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StudentMapper extends EntityMapper <StudentDTO, Student> {
    
    

}
