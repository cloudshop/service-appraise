package com.eyun.appraise.service.mapper;

import com.eyun.appraise.domain.*;
import com.eyun.appraise.service.dto.AppraiseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Appraise and its DTO AppraiseDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppraiseMapper extends EntityMapper<AppraiseDTO, Appraise> {



    default Appraise fromId(Long id) {
        if (id == null) {
            return null;
        }
        Appraise appraise = new Appraise();
        appraise.setId(id);
        return appraise;
    }
}
