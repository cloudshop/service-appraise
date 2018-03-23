package com.eyun.appraise.repository;

import com.eyun.appraise.domain.Appraise;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Appraise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppraiseRepository extends JpaRepository<Appraise, Long>, JpaSpecificationExecutor<Appraise> {

}
