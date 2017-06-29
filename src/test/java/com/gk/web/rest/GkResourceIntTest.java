package com.gk.web.rest;

import com.gk.AbstractCassandraTest;
import com.gk.RepoApp;

import com.gk.domain.Gk;
import com.gk.repository.GkRepository;
import com.gk.service.GkService;
import com.gk.service.dto.GkDTO;
import com.gk.service.mapper.GkMapper;
import com.gk.web.rest.errors.ExceptionTranslator;

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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GkResource REST controller.
 *
 * @see GkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepoApp.class)
public class GkResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private GkRepository gkRepository;

    @Autowired
    private GkMapper gkMapper;

    @Autowired
    private GkService gkService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restGkMockMvc;

    private Gk gk;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GkResource gkResource = new GkResource(gkService);
        this.restGkMockMvc = MockMvcBuilders.standaloneSetup(gkResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gk createEntity() {
        Gk gk = new Gk()
            .name(DEFAULT_NAME);
        return gk;
    }

    @Before
    public void initTest() {
        gkRepository.deleteAll();
        gk = createEntity();
    }

    @Test
    public void createGk() throws Exception {
        int databaseSizeBeforeCreate = gkRepository.findAll().size();

        // Create the Gk
        GkDTO gkDTO = gkMapper.toDto(gk);
        restGkMockMvc.perform(post("/api/gks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gkDTO)))
            .andExpect(status().isCreated());

        // Validate the Gk in the database
        List<Gk> gkList = gkRepository.findAll();
        assertThat(gkList).hasSize(databaseSizeBeforeCreate + 1);
        Gk testGk = gkList.get(gkList.size() - 1);
        assertThat(testGk.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createGkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gkRepository.findAll().size();

        // Create the Gk with an existing ID
        gk.setId(UUID.randomUUID());
        GkDTO gkDTO = gkMapper.toDto(gk);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGkMockMvc.perform(post("/api/gks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Gk> gkList = gkRepository.findAll();
        assertThat(gkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gkRepository.findAll().size();
        // set the field null
        gk.setName(null);

        // Create the Gk, which fails.
        GkDTO gkDTO = gkMapper.toDto(gk);

        restGkMockMvc.perform(post("/api/gks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gkDTO)))
            .andExpect(status().isBadRequest());

        List<Gk> gkList = gkRepository.findAll();
        assertThat(gkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllGks() throws Exception {
        // Initialize the database
        gkRepository.save(gk);

        // Get all the gkList
        restGkMockMvc.perform(get("/api/gks"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gk.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    public void getGk() throws Exception {
        // Initialize the database
        gkRepository.save(gk);

        // Get the gk
        restGkMockMvc.perform(get("/api/gks/{id}", gk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gk.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingGk() throws Exception {
        // Get the gk
        restGkMockMvc.perform(get("/api/gks/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateGk() throws Exception {
        // Initialize the database
        gkRepository.save(gk);
        int databaseSizeBeforeUpdate = gkRepository.findAll().size();

        // Update the gk
        Gk updatedGk = gkRepository.findOne(gk.getId());
        updatedGk
            .name(UPDATED_NAME);
        GkDTO gkDTO = gkMapper.toDto(updatedGk);

        restGkMockMvc.perform(put("/api/gks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gkDTO)))
            .andExpect(status().isOk());

        // Validate the Gk in the database
        List<Gk> gkList = gkRepository.findAll();
        assertThat(gkList).hasSize(databaseSizeBeforeUpdate);
        Gk testGk = gkList.get(gkList.size() - 1);
        assertThat(testGk.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingGk() throws Exception {
        int databaseSizeBeforeUpdate = gkRepository.findAll().size();

        // Create the Gk
        GkDTO gkDTO = gkMapper.toDto(gk);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGkMockMvc.perform(put("/api/gks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gkDTO)))
            .andExpect(status().isCreated());

        // Validate the Gk in the database
        List<Gk> gkList = gkRepository.findAll();
        assertThat(gkList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteGk() throws Exception {
        // Initialize the database
        gkRepository.save(gk);
        int databaseSizeBeforeDelete = gkRepository.findAll().size();

        // Get the gk
        restGkMockMvc.perform(delete("/api/gks/{id}", gk.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Gk> gkList = gkRepository.findAll();
        assertThat(gkList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gk.class);
        Gk gk1 = new Gk();
        gk1.setId(UUID.randomUUID());
        Gk gk2 = new Gk();
        gk2.setId(gk1.getId());
        assertThat(gk1).isEqualTo(gk2);
        gk2.setId(UUID.randomUUID());
        assertThat(gk1).isNotEqualTo(gk2);
        gk1.setId(null);
        assertThat(gk1).isNotEqualTo(gk2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GkDTO.class);
        GkDTO gkDTO1 = new GkDTO();
        gkDTO1.setId(UUID.randomUUID());
        GkDTO gkDTO2 = new GkDTO();
        assertThat(gkDTO1).isNotEqualTo(gkDTO2);
        gkDTO2.setId(gkDTO1.getId());
        assertThat(gkDTO1).isEqualTo(gkDTO2);
        gkDTO2.setId(UUID.randomUUID());
        assertThat(gkDTO1).isNotEqualTo(gkDTO2);
        gkDTO1.setId(null);
        assertThat(gkDTO1).isNotEqualTo(gkDTO2);
    }
}
