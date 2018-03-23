package com.eyun.appraise.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.eyun.appraise.domain.Appraise;
import com.eyun.appraise.domain.*; // for static metamodels
import com.eyun.appraise.repository.AppraiseRepository;
import com.eyun.appraise.service.dto.AppraiseCriteria;

import com.eyun.appraise.service.dto.AppraiseDTO;
import com.eyun.appraise.service.mapper.AppraiseMapper;

/**
 * Service for executing complex queries for Appraise entities in the database.
 * The main input is a {@link AppraiseCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppraiseDTO} or a {@link Page} of {@link AppraiseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppraiseQueryService extends QueryService<Appraise> {

    private final Logger log = LoggerFactory.getLogger(AppraiseQueryService.class);


    private final AppraiseRepository appraiseRepository;

    private final AppraiseMapper appraiseMapper;

    public AppraiseQueryService(AppraiseRepository appraiseRepository, AppraiseMapper appraiseMapper) {
        this.appraiseRepository = appraiseRepository;
        this.appraiseMapper = appraiseMapper;
    }

    /**
     * Return a {@link List} of {@link AppraiseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppraiseDTO> findByCriteria(AppraiseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Appraise> specification = createSpecification(criteria);
        return appraiseMapper.toDto(appraiseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AppraiseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppraiseDTO> findByCriteria(AppraiseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Appraise> specification = createSpecification(criteria);
        final Page<Appraise> result = appraiseRepository.findAll(specification, page);
        return result.map(appraiseMapper::toDto);
    }

    /**
     * Function to convert AppraiseCriteria to a {@link Specifications}
     */
    private Specifications<Appraise> createSpecification(AppraiseCriteria criteria) {
        Specifications<Appraise> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Appraise_.id));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), Appraise_.comment));
            }
            if (criteria.getProductLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProductLevel(), Appraise_.productLevel));
            }
            if (criteria.getPackageLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPackageLevel(), Appraise_.packageLevel));
            }
            if (criteria.getDeliveryLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryLevel(), Appraise_.deliveryLevel));
            }
            if (criteria.getDeliverymanLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliverymanLevel(), Appraise_.deliverymanLevel));
            }
            if (criteria.getImages() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImages(), Appraise_.images));
            }
            if (criteria.getUserid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserid(), Appraise_.userid));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShopId(), Appraise_.shopId));
            }
            if (criteria.getProId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProId(), Appraise_.proId));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderId(), Appraise_.orderId));
            }
            if (criteria.getBuyTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBuyTime(), Appraise_.buyTime));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), Appraise_.createdTime));
            }
            if (criteria.getUpdatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedTime(), Appraise_.updatedTime));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeleted(), Appraise_.deleted));
            }
        }
        return specification;
    }

}
