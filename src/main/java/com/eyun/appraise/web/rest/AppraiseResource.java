package com.eyun.appraise.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.appraise.service.AppraiseService;
import com.eyun.appraise.service.UaaService;
import com.eyun.appraise.web.rest.errors.BadRequestAlertException;
import com.eyun.appraise.web.rest.util.HeaderUtil;
import com.eyun.appraise.web.rest.util.PaginationUtil;
import com.eyun.appraise.service.dto.AppraiseDTO;
import com.eyun.appraise.service.dto.ApraDTO;
import com.eyun.appraise.service.dto.UserDTO;
import com.eyun.appraise.service.dto.AppraiseCriteria;
import com.eyun.appraise.domain.Appraise;
import com.eyun.appraise.service.AppraiseQueryService;

import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Appraise.
 */
@RestController
@RequestMapping("/api")
public class AppraiseResource {

    private final Logger log = LoggerFactory.getLogger(AppraiseResource.class);

    private static final String ENTITY_NAME = "appraise";

    private final AppraiseService appraiseService;

    private final AppraiseQueryService appraiseQueryService;
    
    @Autowired
    private  UaaService uaaService;

    public AppraiseResource(AppraiseService appraiseService, AppraiseQueryService appraiseQueryService) {
        this.appraiseService = appraiseService;
        this.appraiseQueryService = appraiseQueryService;
    }

    /**
     * POST  /appraises : Create a new appraise.
     *
     * @param appraiseDTO the appraiseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appraiseDTO, or with status 400 (Bad Request) if the appraise has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/appraises")
    @Timed
    public ResponseEntity<AppraiseDTO> createAppraise(@Valid @RequestBody AppraiseDTO appraiseDTO) throws URISyntaxException {
    	// 判断订单 提交过后 通知
        log.debug("REST request to save Appraise : {}", appraiseDTO);
        if (appraiseDTO.getId() != null) {
            throw new BadRequestAlertException("A new appraise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
      	UserDTO userDTO;
      	 try {
      		 userDTO=uaaService.getAccount();	
    			  } catch (Exception e) {
    				  throw new BadRequestAlertException("获取当前用户失败", "", "");
    		 }
      	appraiseDTO.setUserid(userDTO.getId());
      	appraiseDTO.setDeleted(0);
      	appraiseDTO.setCreatedTime(Instant.now());
      	appraiseDTO.setUpdatedTime(Instant.now());
        AppraiseDTO result = appraiseService.save(appraiseDTO);
        return ResponseEntity.created(new URI("/api/appraises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /appraises : Updates an existing appraise.
     *
     * @param appraiseDTO the appraiseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appraiseDTO,
     * or with status 400 (Bad Request) if the appraiseDTO is not valid,
     * or with status 500 (Internal Server Error) if the appraiseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/appraises")
    @Timed
    public ResponseEntity<AppraiseDTO> updateAppraise(@Valid @RequestBody AppraiseDTO appraiseDTO) throws URISyntaxException {
        log.debug("REST request to update Appraise : {}", appraiseDTO);
        if (appraiseDTO.getId() == null) {
            return createAppraise(appraiseDTO);
        }
        AppraiseDTO result = appraiseService.save(appraiseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appraiseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /appraises : get all the appraises.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of appraises in body
     */
    @GetMapping("/appraises")
    @Timed
    public ResponseEntity<List<AppraiseDTO>> getAllAppraises(AppraiseCriteria criteria, Pageable pageable) {
        Page<AppraiseDTO> page = appraiseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appraises");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /appraises/:id : get the "id" appraise.
     *
     * @param id the id of the appraiseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appraiseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/appraises/{id}")
    @Timed
    public ResponseEntity<AppraiseDTO> getAppraise(@PathVariable Long id) {
        log.debug("REST request to get Appraise : {}", id);
        AppraiseDTO appraiseDTO = appraiseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(appraiseDTO));
    }

    /**
     * DELETE  /appraises/:id : delete the "id" appraise.
     *
     * @param id the id of the appraiseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/appraises/{id}")
    @Timed
    public ResponseEntity<Void> deleteAppraise(@PathVariable Long id) {
        log.debug("REST request to delete Appraise : {}", id);
        appraiseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @ApiOperation("根据商品查评论")
    @PostMapping("/appraises")
    public ResponseEntity<List<AppraiseDTO>> findByProId(@RequestBody ApraDTO apraDTO){
		Pageable pageable = new PageRequest(apraDTO.getPage(),apraDTO.getSize(),new Sort(new Order(Direction.DESC,"createdTime")));
		//productID
		LongFilter longFilter = new LongFilter();
		longFilter.setEquals(apraDTO.getProductId());
		IntegerFilter filter = new IntegerFilter();
		filter.setEquals(0);
		AppraiseCriteria criteria = new AppraiseCriteria();
		criteria.setProId(longFilter);
		criteria.setDeleted(filter);
		Page<AppraiseDTO> findByCriteria = appraiseQueryService.findByCriteria(criteria, pageable);
		List<AppraiseDTO> content = findByCriteria.getContent();
		return new ResponseEntity<>(content,HttpStatus.OK);
    }
    
    
}
