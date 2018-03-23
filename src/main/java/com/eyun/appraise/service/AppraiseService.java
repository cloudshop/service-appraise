package com.eyun.appraise.service;

import com.eyun.appraise.service.dto.AppraiseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Appraise.
 */
public interface AppraiseService {

    /**
     * Save a appraise.
     *
     * @param appraiseDTO the entity to save
     * @return the persisted entity
     */
    AppraiseDTO save(AppraiseDTO appraiseDTO);

    /**
     * Get all the appraises.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AppraiseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" appraise.
     *
     * @param id the id of the entity
     * @return the entity
     */
    AppraiseDTO findOne(Long id);

    /**
     * Delete the "id" appraise.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
