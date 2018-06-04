package com.eyun.appraise.service.impl;

import com.eyun.appraise.service.AppraiseService;
import com.eyun.appraise.domain.Appraise;
import com.eyun.appraise.repository.AppraiseRepository;
import com.eyun.appraise.service.dto.AppraiseDTO;
import com.eyun.appraise.service.mapper.AppraiseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Appraise.
 */
@Service
@Transactional
public class AppraiseServiceImpl implements AppraiseService {

    private final Logger log = LoggerFactory.getLogger(AppraiseServiceImpl.class);

    private final AppraiseRepository appraiseRepository;

    private final AppraiseMapper appraiseMapper;

    public AppraiseServiceImpl(AppraiseRepository appraiseRepository, AppraiseMapper appraiseMapper) {
        this.appraiseRepository = appraiseRepository;
        this.appraiseMapper = appraiseMapper;
    }

    /**
     * Save a appraise.
     *
     * @param appraiseDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AppraiseDTO save(AppraiseDTO appraiseDTO) {
        log.debug("Request to save Appraise : {}", appraiseDTO);
        Appraise appraise = appraiseMapper.toEntity(appraiseDTO);
        appraise = appraiseRepository.save(appraise);
        return appraiseMapper.toDto(appraise);
    }

    /**
     * Get all the appraises.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AppraiseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Appraises");
        return appraiseRepository.findAll(pageable)
            .map(appraiseMapper::toDto);
    }

    /**
     * Get one appraise by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AppraiseDTO findOne(Long id) {
        log.debug("Request to get Appraise : {}", id);
        Appraise appraise = appraiseRepository.findOne(id);
        return appraiseMapper.toDto(appraise);
    }

    /**
     * Delete the appraise by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Appraise : {}", id);
        /**
         * 物理删除
         */
        Appraise appraise = appraiseRepository.findOne(id);
        appraise.setDeleted(1);
        appraiseRepository.save(appraise);
        return ;
    }
}
