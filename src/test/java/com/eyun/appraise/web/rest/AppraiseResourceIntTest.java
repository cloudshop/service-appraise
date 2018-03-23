package com.eyun.appraise.web.rest;

import com.eyun.appraise.AppraiseApp;

import com.eyun.appraise.config.SecurityBeanOverrideConfiguration;

import com.eyun.appraise.domain.Appraise;
import com.eyun.appraise.repository.AppraiseRepository;
import com.eyun.appraise.service.AppraiseService;
import com.eyun.appraise.service.dto.AppraiseDTO;
import com.eyun.appraise.service.mapper.AppraiseMapper;
import com.eyun.appraise.web.rest.errors.ExceptionTranslator;
import com.eyun.appraise.service.dto.AppraiseCriteria;
import com.eyun.appraise.service.AppraiseQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.eyun.appraise.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AppraiseResource REST controller.
 *
 * @see AppraiseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AppraiseApp.class, SecurityBeanOverrideConfiguration.class})
public class AppraiseResourceIntTest {

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRODUCT_LEVEL = 1;
    private static final Integer UPDATED_PRODUCT_LEVEL = 2;

    private static final Integer DEFAULT_PACKAGE_LEVEL = 1;
    private static final Integer UPDATED_PACKAGE_LEVEL = 2;

    private static final Integer DEFAULT_DELIVERY_LEVEL = 1;
    private static final Integer UPDATED_DELIVERY_LEVEL = 2;

    private static final Integer DEFAULT_DELIVERYMAN_LEVEL = 1;
    private static final Integer UPDATED_DELIVERYMAN_LEVEL = 2;

    private static final String DEFAULT_IMAGES = "AAAAAAAAAA";
    private static final String UPDATED_IMAGES = "BBBBBBBBBB";

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    private static final Long DEFAULT_SHOP_ID = 1L;
    private static final Long UPDATED_SHOP_ID = 2L;

    private static final Long DEFAULT_PRO_ID = 1L;
    private static final Long UPDATED_PRO_ID = 2L;

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;

    private static final Instant DEFAULT_BUY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BUY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    @Autowired
    private AppraiseRepository appraiseRepository;

    @Autowired
    private AppraiseMapper appraiseMapper;

    @Autowired
    private AppraiseService appraiseService;

    @Autowired
    private AppraiseQueryService appraiseQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppraiseMockMvc;

    private Appraise appraise;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppraiseResource appraiseResource = new AppraiseResource(appraiseService, appraiseQueryService);
        this.restAppraiseMockMvc = MockMvcBuilders.standaloneSetup(appraiseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appraise createEntity(EntityManager em) {
        Appraise appraise = new Appraise()
            .comment(DEFAULT_COMMENT)
            .productLevel(DEFAULT_PRODUCT_LEVEL)
            .packageLevel(DEFAULT_PACKAGE_LEVEL)
            .deliveryLevel(DEFAULT_DELIVERY_LEVEL)
            .deliverymanLevel(DEFAULT_DELIVERYMAN_LEVEL)
            .images(DEFAULT_IMAGES)
            .userid(DEFAULT_USERID)
            .shopId(DEFAULT_SHOP_ID)
            .proId(DEFAULT_PRO_ID)
            .orderId(DEFAULT_ORDER_ID)
            .buyTime(DEFAULT_BUY_TIME)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .deleted(DEFAULT_DELETED);
        return appraise;
    }

    @Before
    public void initTest() {
        appraise = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppraise() throws Exception {
        int databaseSizeBeforeCreate = appraiseRepository.findAll().size();

        // Create the Appraise
        AppraiseDTO appraiseDTO = appraiseMapper.toDto(appraise);
        restAppraiseMockMvc.perform(post("/api/appraises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appraiseDTO)))
            .andExpect(status().isCreated());

        // Validate the Appraise in the database
        List<Appraise> appraiseList = appraiseRepository.findAll();
        assertThat(appraiseList).hasSize(databaseSizeBeforeCreate + 1);
        Appraise testAppraise = appraiseList.get(appraiseList.size() - 1);
        assertThat(testAppraise.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testAppraise.getProductLevel()).isEqualTo(DEFAULT_PRODUCT_LEVEL);
        assertThat(testAppraise.getPackageLevel()).isEqualTo(DEFAULT_PACKAGE_LEVEL);
        assertThat(testAppraise.getDeliveryLevel()).isEqualTo(DEFAULT_DELIVERY_LEVEL);
        assertThat(testAppraise.getDeliverymanLevel()).isEqualTo(DEFAULT_DELIVERYMAN_LEVEL);
        assertThat(testAppraise.getImages()).isEqualTo(DEFAULT_IMAGES);
        assertThat(testAppraise.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testAppraise.getShopId()).isEqualTo(DEFAULT_SHOP_ID);
        assertThat(testAppraise.getProId()).isEqualTo(DEFAULT_PRO_ID);
        assertThat(testAppraise.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testAppraise.getBuyTime()).isEqualTo(DEFAULT_BUY_TIME);
        assertThat(testAppraise.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testAppraise.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testAppraise.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createAppraiseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appraiseRepository.findAll().size();

        // Create the Appraise with an existing ID
        appraise.setId(1L);
        AppraiseDTO appraiseDTO = appraiseMapper.toDto(appraise);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppraiseMockMvc.perform(post("/api/appraises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appraiseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Appraise in the database
        List<Appraise> appraiseList = appraiseRepository.findAll();
        assertThat(appraiseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = appraiseRepository.findAll().size();
        // set the field null
        appraise.setComment(null);

        // Create the Appraise, which fails.
        AppraiseDTO appraiseDTO = appraiseMapper.toDto(appraise);

        restAppraiseMockMvc.perform(post("/api/appraises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appraiseDTO)))
            .andExpect(status().isBadRequest());

        List<Appraise> appraiseList = appraiseRepository.findAll();
        assertThat(appraiseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUseridIsRequired() throws Exception {
        int databaseSizeBeforeTest = appraiseRepository.findAll().size();
        // set the field null
        appraise.setUserid(null);

        // Create the Appraise, which fails.
        AppraiseDTO appraiseDTO = appraiseMapper.toDto(appraise);

        restAppraiseMockMvc.perform(post("/api/appraises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appraiseDTO)))
            .andExpect(status().isBadRequest());

        List<Appraise> appraiseList = appraiseRepository.findAll();
        assertThat(appraiseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShopIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = appraiseRepository.findAll().size();
        // set the field null
        appraise.setShopId(null);

        // Create the Appraise, which fails.
        AppraiseDTO appraiseDTO = appraiseMapper.toDto(appraise);

        restAppraiseMockMvc.perform(post("/api/appraises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appraiseDTO)))
            .andExpect(status().isBadRequest());

        List<Appraise> appraiseList = appraiseRepository.findAll();
        assertThat(appraiseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = appraiseRepository.findAll().size();
        // set the field null
        appraise.setProId(null);

        // Create the Appraise, which fails.
        AppraiseDTO appraiseDTO = appraiseMapper.toDto(appraise);

        restAppraiseMockMvc.perform(post("/api/appraises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appraiseDTO)))
            .andExpect(status().isBadRequest());

        List<Appraise> appraiseList = appraiseRepository.findAll();
        assertThat(appraiseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = appraiseRepository.findAll().size();
        // set the field null
        appraise.setOrderId(null);

        // Create the Appraise, which fails.
        AppraiseDTO appraiseDTO = appraiseMapper.toDto(appraise);

        restAppraiseMockMvc.perform(post("/api/appraises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appraiseDTO)))
            .andExpect(status().isBadRequest());

        List<Appraise> appraiseList = appraiseRepository.findAll();
        assertThat(appraiseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBuyTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = appraiseRepository.findAll().size();
        // set the field null
        appraise.setBuyTime(null);

        // Create the Appraise, which fails.
        AppraiseDTO appraiseDTO = appraiseMapper.toDto(appraise);

        restAppraiseMockMvc.perform(post("/api/appraises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appraiseDTO)))
            .andExpect(status().isBadRequest());

        List<Appraise> appraiseList = appraiseRepository.findAll();
        assertThat(appraiseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppraises() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList
        restAppraiseMockMvc.perform(get("/api/appraises?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appraise.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].productLevel").value(hasItem(DEFAULT_PRODUCT_LEVEL)))
            .andExpect(jsonPath("$.[*].packageLevel").value(hasItem(DEFAULT_PACKAGE_LEVEL)))
            .andExpect(jsonPath("$.[*].deliveryLevel").value(hasItem(DEFAULT_DELIVERY_LEVEL)))
            .andExpect(jsonPath("$.[*].deliverymanLevel").value(hasItem(DEFAULT_DELIVERYMAN_LEVEL)))
            .andExpect(jsonPath("$.[*].images").value(hasItem(DEFAULT_IMAGES.toString())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].shopId").value(hasItem(DEFAULT_SHOP_ID.intValue())))
            .andExpect(jsonPath("$.[*].proId").value(hasItem(DEFAULT_PRO_ID.intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].buyTime").value(hasItem(DEFAULT_BUY_TIME.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    public void getAppraise() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get the appraise
        restAppraiseMockMvc.perform(get("/api/appraises/{id}", appraise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appraise.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.productLevel").value(DEFAULT_PRODUCT_LEVEL))
            .andExpect(jsonPath("$.packageLevel").value(DEFAULT_PACKAGE_LEVEL))
            .andExpect(jsonPath("$.deliveryLevel").value(DEFAULT_DELIVERY_LEVEL))
            .andExpect(jsonPath("$.deliverymanLevel").value(DEFAULT_DELIVERYMAN_LEVEL))
            .andExpect(jsonPath("$.images").value(DEFAULT_IMAGES.toString()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()))
            .andExpect(jsonPath("$.shopId").value(DEFAULT_SHOP_ID.intValue()))
            .andExpect(jsonPath("$.proId").value(DEFAULT_PRO_ID.intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.buyTime").value(DEFAULT_BUY_TIME.toString()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    public void getAllAppraisesByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where comment equals to DEFAULT_COMMENT
        defaultAppraiseShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the appraiseList where comment equals to UPDATED_COMMENT
        defaultAppraiseShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllAppraisesByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultAppraiseShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the appraiseList where comment equals to UPDATED_COMMENT
        defaultAppraiseShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllAppraisesByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where comment is not null
        defaultAppraiseShouldBeFound("comment.specified=true");

        // Get all the appraiseList where comment is null
        defaultAppraiseShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByProductLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where productLevel equals to DEFAULT_PRODUCT_LEVEL
        defaultAppraiseShouldBeFound("productLevel.equals=" + DEFAULT_PRODUCT_LEVEL);

        // Get all the appraiseList where productLevel equals to UPDATED_PRODUCT_LEVEL
        defaultAppraiseShouldNotBeFound("productLevel.equals=" + UPDATED_PRODUCT_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAppraisesByProductLevelIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where productLevel in DEFAULT_PRODUCT_LEVEL or UPDATED_PRODUCT_LEVEL
        defaultAppraiseShouldBeFound("productLevel.in=" + DEFAULT_PRODUCT_LEVEL + "," + UPDATED_PRODUCT_LEVEL);

        // Get all the appraiseList where productLevel equals to UPDATED_PRODUCT_LEVEL
        defaultAppraiseShouldNotBeFound("productLevel.in=" + UPDATED_PRODUCT_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAppraisesByProductLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where productLevel is not null
        defaultAppraiseShouldBeFound("productLevel.specified=true");

        // Get all the appraiseList where productLevel is null
        defaultAppraiseShouldNotBeFound("productLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByProductLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where productLevel greater than or equals to DEFAULT_PRODUCT_LEVEL
        defaultAppraiseShouldBeFound("productLevel.greaterOrEqualThan=" + DEFAULT_PRODUCT_LEVEL);

        // Get all the appraiseList where productLevel greater than or equals to UPDATED_PRODUCT_LEVEL
        defaultAppraiseShouldNotBeFound("productLevel.greaterOrEqualThan=" + UPDATED_PRODUCT_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAppraisesByProductLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where productLevel less than or equals to DEFAULT_PRODUCT_LEVEL
        defaultAppraiseShouldNotBeFound("productLevel.lessThan=" + DEFAULT_PRODUCT_LEVEL);

        // Get all the appraiseList where productLevel less than or equals to UPDATED_PRODUCT_LEVEL
        defaultAppraiseShouldBeFound("productLevel.lessThan=" + UPDATED_PRODUCT_LEVEL);
    }


    @Test
    @Transactional
    public void getAllAppraisesByPackageLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where packageLevel equals to DEFAULT_PACKAGE_LEVEL
        defaultAppraiseShouldBeFound("packageLevel.equals=" + DEFAULT_PACKAGE_LEVEL);

        // Get all the appraiseList where packageLevel equals to UPDATED_PACKAGE_LEVEL
        defaultAppraiseShouldNotBeFound("packageLevel.equals=" + UPDATED_PACKAGE_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAppraisesByPackageLevelIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where packageLevel in DEFAULT_PACKAGE_LEVEL or UPDATED_PACKAGE_LEVEL
        defaultAppraiseShouldBeFound("packageLevel.in=" + DEFAULT_PACKAGE_LEVEL + "," + UPDATED_PACKAGE_LEVEL);

        // Get all the appraiseList where packageLevel equals to UPDATED_PACKAGE_LEVEL
        defaultAppraiseShouldNotBeFound("packageLevel.in=" + UPDATED_PACKAGE_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAppraisesByPackageLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where packageLevel is not null
        defaultAppraiseShouldBeFound("packageLevel.specified=true");

        // Get all the appraiseList where packageLevel is null
        defaultAppraiseShouldNotBeFound("packageLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByPackageLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where packageLevel greater than or equals to DEFAULT_PACKAGE_LEVEL
        defaultAppraiseShouldBeFound("packageLevel.greaterOrEqualThan=" + DEFAULT_PACKAGE_LEVEL);

        // Get all the appraiseList where packageLevel greater than or equals to UPDATED_PACKAGE_LEVEL
        defaultAppraiseShouldNotBeFound("packageLevel.greaterOrEqualThan=" + UPDATED_PACKAGE_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAppraisesByPackageLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where packageLevel less than or equals to DEFAULT_PACKAGE_LEVEL
        defaultAppraiseShouldNotBeFound("packageLevel.lessThan=" + DEFAULT_PACKAGE_LEVEL);

        // Get all the appraiseList where packageLevel less than or equals to UPDATED_PACKAGE_LEVEL
        defaultAppraiseShouldBeFound("packageLevel.lessThan=" + UPDATED_PACKAGE_LEVEL);
    }


    @Test
    @Transactional
    public void getAllAppraisesByDeliveryLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deliveryLevel equals to DEFAULT_DELIVERY_LEVEL
        defaultAppraiseShouldBeFound("deliveryLevel.equals=" + DEFAULT_DELIVERY_LEVEL);

        // Get all the appraiseList where deliveryLevel equals to UPDATED_DELIVERY_LEVEL
        defaultAppraiseShouldNotBeFound("deliveryLevel.equals=" + UPDATED_DELIVERY_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeliveryLevelIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deliveryLevel in DEFAULT_DELIVERY_LEVEL or UPDATED_DELIVERY_LEVEL
        defaultAppraiseShouldBeFound("deliveryLevel.in=" + DEFAULT_DELIVERY_LEVEL + "," + UPDATED_DELIVERY_LEVEL);

        // Get all the appraiseList where deliveryLevel equals to UPDATED_DELIVERY_LEVEL
        defaultAppraiseShouldNotBeFound("deliveryLevel.in=" + UPDATED_DELIVERY_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeliveryLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deliveryLevel is not null
        defaultAppraiseShouldBeFound("deliveryLevel.specified=true");

        // Get all the appraiseList where deliveryLevel is null
        defaultAppraiseShouldNotBeFound("deliveryLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeliveryLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deliveryLevel greater than or equals to DEFAULT_DELIVERY_LEVEL
        defaultAppraiseShouldBeFound("deliveryLevel.greaterOrEqualThan=" + DEFAULT_DELIVERY_LEVEL);

        // Get all the appraiseList where deliveryLevel greater than or equals to UPDATED_DELIVERY_LEVEL
        defaultAppraiseShouldNotBeFound("deliveryLevel.greaterOrEqualThan=" + UPDATED_DELIVERY_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeliveryLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deliveryLevel less than or equals to DEFAULT_DELIVERY_LEVEL
        defaultAppraiseShouldNotBeFound("deliveryLevel.lessThan=" + DEFAULT_DELIVERY_LEVEL);

        // Get all the appraiseList where deliveryLevel less than or equals to UPDATED_DELIVERY_LEVEL
        defaultAppraiseShouldBeFound("deliveryLevel.lessThan=" + UPDATED_DELIVERY_LEVEL);
    }


    @Test
    @Transactional
    public void getAllAppraisesByDeliverymanLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deliverymanLevel equals to DEFAULT_DELIVERYMAN_LEVEL
        defaultAppraiseShouldBeFound("deliverymanLevel.equals=" + DEFAULT_DELIVERYMAN_LEVEL);

        // Get all the appraiseList where deliverymanLevel equals to UPDATED_DELIVERYMAN_LEVEL
        defaultAppraiseShouldNotBeFound("deliverymanLevel.equals=" + UPDATED_DELIVERYMAN_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeliverymanLevelIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deliverymanLevel in DEFAULT_DELIVERYMAN_LEVEL or UPDATED_DELIVERYMAN_LEVEL
        defaultAppraiseShouldBeFound("deliverymanLevel.in=" + DEFAULT_DELIVERYMAN_LEVEL + "," + UPDATED_DELIVERYMAN_LEVEL);

        // Get all the appraiseList where deliverymanLevel equals to UPDATED_DELIVERYMAN_LEVEL
        defaultAppraiseShouldNotBeFound("deliverymanLevel.in=" + UPDATED_DELIVERYMAN_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeliverymanLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deliverymanLevel is not null
        defaultAppraiseShouldBeFound("deliverymanLevel.specified=true");

        // Get all the appraiseList where deliverymanLevel is null
        defaultAppraiseShouldNotBeFound("deliverymanLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeliverymanLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deliverymanLevel greater than or equals to DEFAULT_DELIVERYMAN_LEVEL
        defaultAppraiseShouldBeFound("deliverymanLevel.greaterOrEqualThan=" + DEFAULT_DELIVERYMAN_LEVEL);

        // Get all the appraiseList where deliverymanLevel greater than or equals to UPDATED_DELIVERYMAN_LEVEL
        defaultAppraiseShouldNotBeFound("deliverymanLevel.greaterOrEqualThan=" + UPDATED_DELIVERYMAN_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeliverymanLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deliverymanLevel less than or equals to DEFAULT_DELIVERYMAN_LEVEL
        defaultAppraiseShouldNotBeFound("deliverymanLevel.lessThan=" + DEFAULT_DELIVERYMAN_LEVEL);

        // Get all the appraiseList where deliverymanLevel less than or equals to UPDATED_DELIVERYMAN_LEVEL
        defaultAppraiseShouldBeFound("deliverymanLevel.lessThan=" + UPDATED_DELIVERYMAN_LEVEL);
    }


    @Test
    @Transactional
    public void getAllAppraisesByImagesIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where images equals to DEFAULT_IMAGES
        defaultAppraiseShouldBeFound("images.equals=" + DEFAULT_IMAGES);

        // Get all the appraiseList where images equals to UPDATED_IMAGES
        defaultAppraiseShouldNotBeFound("images.equals=" + UPDATED_IMAGES);
    }

    @Test
    @Transactional
    public void getAllAppraisesByImagesIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where images in DEFAULT_IMAGES or UPDATED_IMAGES
        defaultAppraiseShouldBeFound("images.in=" + DEFAULT_IMAGES + "," + UPDATED_IMAGES);

        // Get all the appraiseList where images equals to UPDATED_IMAGES
        defaultAppraiseShouldNotBeFound("images.in=" + UPDATED_IMAGES);
    }

    @Test
    @Transactional
    public void getAllAppraisesByImagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where images is not null
        defaultAppraiseShouldBeFound("images.specified=true");

        // Get all the appraiseList where images is null
        defaultAppraiseShouldNotBeFound("images.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where userid equals to DEFAULT_USERID
        defaultAppraiseShouldBeFound("userid.equals=" + DEFAULT_USERID);

        // Get all the appraiseList where userid equals to UPDATED_USERID
        defaultAppraiseShouldNotBeFound("userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllAppraisesByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where userid in DEFAULT_USERID or UPDATED_USERID
        defaultAppraiseShouldBeFound("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID);

        // Get all the appraiseList where userid equals to UPDATED_USERID
        defaultAppraiseShouldNotBeFound("userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllAppraisesByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where userid is not null
        defaultAppraiseShouldBeFound("userid.specified=true");

        // Get all the appraiseList where userid is null
        defaultAppraiseShouldNotBeFound("userid.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByUseridIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where userid greater than or equals to DEFAULT_USERID
        defaultAppraiseShouldBeFound("userid.greaterOrEqualThan=" + DEFAULT_USERID);

        // Get all the appraiseList where userid greater than or equals to UPDATED_USERID
        defaultAppraiseShouldNotBeFound("userid.greaterOrEqualThan=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllAppraisesByUseridIsLessThanSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where userid less than or equals to DEFAULT_USERID
        defaultAppraiseShouldNotBeFound("userid.lessThan=" + DEFAULT_USERID);

        // Get all the appraiseList where userid less than or equals to UPDATED_USERID
        defaultAppraiseShouldBeFound("userid.lessThan=" + UPDATED_USERID);
    }


    @Test
    @Transactional
    public void getAllAppraisesByShopIdIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where shopId equals to DEFAULT_SHOP_ID
        defaultAppraiseShouldBeFound("shopId.equals=" + DEFAULT_SHOP_ID);

        // Get all the appraiseList where shopId equals to UPDATED_SHOP_ID
        defaultAppraiseShouldNotBeFound("shopId.equals=" + UPDATED_SHOP_ID);
    }

    @Test
    @Transactional
    public void getAllAppraisesByShopIdIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where shopId in DEFAULT_SHOP_ID or UPDATED_SHOP_ID
        defaultAppraiseShouldBeFound("shopId.in=" + DEFAULT_SHOP_ID + "," + UPDATED_SHOP_ID);

        // Get all the appraiseList where shopId equals to UPDATED_SHOP_ID
        defaultAppraiseShouldNotBeFound("shopId.in=" + UPDATED_SHOP_ID);
    }

    @Test
    @Transactional
    public void getAllAppraisesByShopIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where shopId is not null
        defaultAppraiseShouldBeFound("shopId.specified=true");

        // Get all the appraiseList where shopId is null
        defaultAppraiseShouldNotBeFound("shopId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByShopIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where shopId greater than or equals to DEFAULT_SHOP_ID
        defaultAppraiseShouldBeFound("shopId.greaterOrEqualThan=" + DEFAULT_SHOP_ID);

        // Get all the appraiseList where shopId greater than or equals to UPDATED_SHOP_ID
        defaultAppraiseShouldNotBeFound("shopId.greaterOrEqualThan=" + UPDATED_SHOP_ID);
    }

    @Test
    @Transactional
    public void getAllAppraisesByShopIdIsLessThanSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where shopId less than or equals to DEFAULT_SHOP_ID
        defaultAppraiseShouldNotBeFound("shopId.lessThan=" + DEFAULT_SHOP_ID);

        // Get all the appraiseList where shopId less than or equals to UPDATED_SHOP_ID
        defaultAppraiseShouldBeFound("shopId.lessThan=" + UPDATED_SHOP_ID);
    }


    @Test
    @Transactional
    public void getAllAppraisesByProIdIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where proId equals to DEFAULT_PRO_ID
        defaultAppraiseShouldBeFound("proId.equals=" + DEFAULT_PRO_ID);

        // Get all the appraiseList where proId equals to UPDATED_PRO_ID
        defaultAppraiseShouldNotBeFound("proId.equals=" + UPDATED_PRO_ID);
    }

    @Test
    @Transactional
    public void getAllAppraisesByProIdIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where proId in DEFAULT_PRO_ID or UPDATED_PRO_ID
        defaultAppraiseShouldBeFound("proId.in=" + DEFAULT_PRO_ID + "," + UPDATED_PRO_ID);

        // Get all the appraiseList where proId equals to UPDATED_PRO_ID
        defaultAppraiseShouldNotBeFound("proId.in=" + UPDATED_PRO_ID);
    }

    @Test
    @Transactional
    public void getAllAppraisesByProIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where proId is not null
        defaultAppraiseShouldBeFound("proId.specified=true");

        // Get all the appraiseList where proId is null
        defaultAppraiseShouldNotBeFound("proId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByProIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where proId greater than or equals to DEFAULT_PRO_ID
        defaultAppraiseShouldBeFound("proId.greaterOrEqualThan=" + DEFAULT_PRO_ID);

        // Get all the appraiseList where proId greater than or equals to UPDATED_PRO_ID
        defaultAppraiseShouldNotBeFound("proId.greaterOrEqualThan=" + UPDATED_PRO_ID);
    }

    @Test
    @Transactional
    public void getAllAppraisesByProIdIsLessThanSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where proId less than or equals to DEFAULT_PRO_ID
        defaultAppraiseShouldNotBeFound("proId.lessThan=" + DEFAULT_PRO_ID);

        // Get all the appraiseList where proId less than or equals to UPDATED_PRO_ID
        defaultAppraiseShouldBeFound("proId.lessThan=" + UPDATED_PRO_ID);
    }


    @Test
    @Transactional
    public void getAllAppraisesByOrderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where orderId equals to DEFAULT_ORDER_ID
        defaultAppraiseShouldBeFound("orderId.equals=" + DEFAULT_ORDER_ID);

        // Get all the appraiseList where orderId equals to UPDATED_ORDER_ID
        defaultAppraiseShouldNotBeFound("orderId.equals=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    public void getAllAppraisesByOrderIdIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where orderId in DEFAULT_ORDER_ID or UPDATED_ORDER_ID
        defaultAppraiseShouldBeFound("orderId.in=" + DEFAULT_ORDER_ID + "," + UPDATED_ORDER_ID);

        // Get all the appraiseList where orderId equals to UPDATED_ORDER_ID
        defaultAppraiseShouldNotBeFound("orderId.in=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    public void getAllAppraisesByOrderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where orderId is not null
        defaultAppraiseShouldBeFound("orderId.specified=true");

        // Get all the appraiseList where orderId is null
        defaultAppraiseShouldNotBeFound("orderId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByOrderIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where orderId greater than or equals to DEFAULT_ORDER_ID
        defaultAppraiseShouldBeFound("orderId.greaterOrEqualThan=" + DEFAULT_ORDER_ID);

        // Get all the appraiseList where orderId greater than or equals to UPDATED_ORDER_ID
        defaultAppraiseShouldNotBeFound("orderId.greaterOrEqualThan=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    public void getAllAppraisesByOrderIdIsLessThanSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where orderId less than or equals to DEFAULT_ORDER_ID
        defaultAppraiseShouldNotBeFound("orderId.lessThan=" + DEFAULT_ORDER_ID);

        // Get all the appraiseList where orderId less than or equals to UPDATED_ORDER_ID
        defaultAppraiseShouldBeFound("orderId.lessThan=" + UPDATED_ORDER_ID);
    }


    @Test
    @Transactional
    public void getAllAppraisesByBuyTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where buyTime equals to DEFAULT_BUY_TIME
        defaultAppraiseShouldBeFound("buyTime.equals=" + DEFAULT_BUY_TIME);

        // Get all the appraiseList where buyTime equals to UPDATED_BUY_TIME
        defaultAppraiseShouldNotBeFound("buyTime.equals=" + UPDATED_BUY_TIME);
    }

    @Test
    @Transactional
    public void getAllAppraisesByBuyTimeIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where buyTime in DEFAULT_BUY_TIME or UPDATED_BUY_TIME
        defaultAppraiseShouldBeFound("buyTime.in=" + DEFAULT_BUY_TIME + "," + UPDATED_BUY_TIME);

        // Get all the appraiseList where buyTime equals to UPDATED_BUY_TIME
        defaultAppraiseShouldNotBeFound("buyTime.in=" + UPDATED_BUY_TIME);
    }

    @Test
    @Transactional
    public void getAllAppraisesByBuyTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where buyTime is not null
        defaultAppraiseShouldBeFound("buyTime.specified=true");

        // Get all the appraiseList where buyTime is null
        defaultAppraiseShouldNotBeFound("buyTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where createdTime equals to DEFAULT_CREATED_TIME
        defaultAppraiseShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the appraiseList where createdTime equals to UPDATED_CREATED_TIME
        defaultAppraiseShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllAppraisesByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultAppraiseShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the appraiseList where createdTime equals to UPDATED_CREATED_TIME
        defaultAppraiseShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllAppraisesByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where createdTime is not null
        defaultAppraiseShouldBeFound("createdTime.specified=true");

        // Get all the appraiseList where createdTime is null
        defaultAppraiseShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByUpdatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where updatedTime equals to DEFAULT_UPDATED_TIME
        defaultAppraiseShouldBeFound("updatedTime.equals=" + DEFAULT_UPDATED_TIME);

        // Get all the appraiseList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultAppraiseShouldNotBeFound("updatedTime.equals=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllAppraisesByUpdatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where updatedTime in DEFAULT_UPDATED_TIME or UPDATED_UPDATED_TIME
        defaultAppraiseShouldBeFound("updatedTime.in=" + DEFAULT_UPDATED_TIME + "," + UPDATED_UPDATED_TIME);

        // Get all the appraiseList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultAppraiseShouldNotBeFound("updatedTime.in=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllAppraisesByUpdatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where updatedTime is not null
        defaultAppraiseShouldBeFound("updatedTime.specified=true");

        // Get all the appraiseList where updatedTime is null
        defaultAppraiseShouldNotBeFound("updatedTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deleted equals to DEFAULT_DELETED
        defaultAppraiseShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the appraiseList where deleted equals to UPDATED_DELETED
        defaultAppraiseShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultAppraiseShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the appraiseList where deleted equals to UPDATED_DELETED
        defaultAppraiseShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deleted is not null
        defaultAppraiseShouldBeFound("deleted.specified=true");

        // Get all the appraiseList where deleted is null
        defaultAppraiseShouldNotBeFound("deleted.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeletedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deleted greater than or equals to DEFAULT_DELETED
        defaultAppraiseShouldBeFound("deleted.greaterOrEqualThan=" + DEFAULT_DELETED);

        // Get all the appraiseList where deleted greater than or equals to UPDATED_DELETED
        defaultAppraiseShouldNotBeFound("deleted.greaterOrEqualThan=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllAppraisesByDeletedIsLessThanSomething() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);

        // Get all the appraiseList where deleted less than or equals to DEFAULT_DELETED
        defaultAppraiseShouldNotBeFound("deleted.lessThan=" + DEFAULT_DELETED);

        // Get all the appraiseList where deleted less than or equals to UPDATED_DELETED
        defaultAppraiseShouldBeFound("deleted.lessThan=" + UPDATED_DELETED);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAppraiseShouldBeFound(String filter) throws Exception {
        restAppraiseMockMvc.perform(get("/api/appraises?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appraise.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].productLevel").value(hasItem(DEFAULT_PRODUCT_LEVEL)))
            .andExpect(jsonPath("$.[*].packageLevel").value(hasItem(DEFAULT_PACKAGE_LEVEL)))
            .andExpect(jsonPath("$.[*].deliveryLevel").value(hasItem(DEFAULT_DELIVERY_LEVEL)))
            .andExpect(jsonPath("$.[*].deliverymanLevel").value(hasItem(DEFAULT_DELIVERYMAN_LEVEL)))
            .andExpect(jsonPath("$.[*].images").value(hasItem(DEFAULT_IMAGES.toString())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].shopId").value(hasItem(DEFAULT_SHOP_ID.intValue())))
            .andExpect(jsonPath("$.[*].proId").value(hasItem(DEFAULT_PRO_ID.intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].buyTime").value(hasItem(DEFAULT_BUY_TIME.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAppraiseShouldNotBeFound(String filter) throws Exception {
        restAppraiseMockMvc.perform(get("/api/appraises?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingAppraise() throws Exception {
        // Get the appraise
        restAppraiseMockMvc.perform(get("/api/appraises/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppraise() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);
        int databaseSizeBeforeUpdate = appraiseRepository.findAll().size();

        // Update the appraise
        Appraise updatedAppraise = appraiseRepository.findOne(appraise.getId());
        // Disconnect from session so that the updates on updatedAppraise are not directly saved in db
        em.detach(updatedAppraise);
        updatedAppraise
            .comment(UPDATED_COMMENT)
            .productLevel(UPDATED_PRODUCT_LEVEL)
            .packageLevel(UPDATED_PACKAGE_LEVEL)
            .deliveryLevel(UPDATED_DELIVERY_LEVEL)
            .deliverymanLevel(UPDATED_DELIVERYMAN_LEVEL)
            .images(UPDATED_IMAGES)
            .userid(UPDATED_USERID)
            .shopId(UPDATED_SHOP_ID)
            .proId(UPDATED_PRO_ID)
            .orderId(UPDATED_ORDER_ID)
            .buyTime(UPDATED_BUY_TIME)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .deleted(UPDATED_DELETED);
        AppraiseDTO appraiseDTO = appraiseMapper.toDto(updatedAppraise);

        restAppraiseMockMvc.perform(put("/api/appraises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appraiseDTO)))
            .andExpect(status().isOk());

        // Validate the Appraise in the database
        List<Appraise> appraiseList = appraiseRepository.findAll();
        assertThat(appraiseList).hasSize(databaseSizeBeforeUpdate);
        Appraise testAppraise = appraiseList.get(appraiseList.size() - 1);
        assertThat(testAppraise.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testAppraise.getProductLevel()).isEqualTo(UPDATED_PRODUCT_LEVEL);
        assertThat(testAppraise.getPackageLevel()).isEqualTo(UPDATED_PACKAGE_LEVEL);
        assertThat(testAppraise.getDeliveryLevel()).isEqualTo(UPDATED_DELIVERY_LEVEL);
        assertThat(testAppraise.getDeliverymanLevel()).isEqualTo(UPDATED_DELIVERYMAN_LEVEL);
        assertThat(testAppraise.getImages()).isEqualTo(UPDATED_IMAGES);
        assertThat(testAppraise.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testAppraise.getShopId()).isEqualTo(UPDATED_SHOP_ID);
        assertThat(testAppraise.getProId()).isEqualTo(UPDATED_PRO_ID);
        assertThat(testAppraise.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testAppraise.getBuyTime()).isEqualTo(UPDATED_BUY_TIME);
        assertThat(testAppraise.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testAppraise.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testAppraise.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingAppraise() throws Exception {
        int databaseSizeBeforeUpdate = appraiseRepository.findAll().size();

        // Create the Appraise
        AppraiseDTO appraiseDTO = appraiseMapper.toDto(appraise);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAppraiseMockMvc.perform(put("/api/appraises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appraiseDTO)))
            .andExpect(status().isCreated());

        // Validate the Appraise in the database
        List<Appraise> appraiseList = appraiseRepository.findAll();
        assertThat(appraiseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAppraise() throws Exception {
        // Initialize the database
        appraiseRepository.saveAndFlush(appraise);
        int databaseSizeBeforeDelete = appraiseRepository.findAll().size();

        // Get the appraise
        restAppraiseMockMvc.perform(delete("/api/appraises/{id}", appraise.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Appraise> appraiseList = appraiseRepository.findAll();
        assertThat(appraiseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appraise.class);
        Appraise appraise1 = new Appraise();
        appraise1.setId(1L);
        Appraise appraise2 = new Appraise();
        appraise2.setId(appraise1.getId());
        assertThat(appraise1).isEqualTo(appraise2);
        appraise2.setId(2L);
        assertThat(appraise1).isNotEqualTo(appraise2);
        appraise1.setId(null);
        assertThat(appraise1).isNotEqualTo(appraise2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppraiseDTO.class);
        AppraiseDTO appraiseDTO1 = new AppraiseDTO();
        appraiseDTO1.setId(1L);
        AppraiseDTO appraiseDTO2 = new AppraiseDTO();
        assertThat(appraiseDTO1).isNotEqualTo(appraiseDTO2);
        appraiseDTO2.setId(appraiseDTO1.getId());
        assertThat(appraiseDTO1).isEqualTo(appraiseDTO2);
        appraiseDTO2.setId(2L);
        assertThat(appraiseDTO1).isNotEqualTo(appraiseDTO2);
        appraiseDTO1.setId(null);
        assertThat(appraiseDTO1).isNotEqualTo(appraiseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(appraiseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(appraiseMapper.fromId(null)).isNull();
    }
}
