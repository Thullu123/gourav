package com.gk.web.rest;

import com.gk.AbstractCassandraTest;
import com.gk.RepoApp;

import com.gk.domain.Gourav;
import com.gk.repository.GouravRepository;
import com.gk.service.GouravService;
import com.gk.service.dto.GouravDTO;
import com.gk.service.mapper.GouravMapper;
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
 * Test class for the GouravResource REST controller.
 *
 * @see GouravResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepoApp.class)
public class GouravResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_KUCHBHI = "AAAAAAAAAA";
    private static final String UPDATED_KUCHBHI = "BBBBBBBBBB";

    @Autowired
    private GouravRepository gouravRepository;

    @Autowired
    private GouravMapper gouravMapper;

    @Autowired
    private GouravService gouravService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restGouravMockMvc;

    private Gourav gourav;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GouravResource gouravResource = new GouravResource(gouravService);
        this.restGouravMockMvc = MockMvcBuilders.standaloneSetup(gouravResource)
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
    public static Gourav createEntity() {
        Gourav gourav = new Gourav()
            .kuchbhi(DEFAULT_KUCHBHI);
        return gourav;
    }

    @Before
    public void initTest() {
        gouravRepository.deleteAll();
        gourav = createEntity();
    }

    @Test
    public void createGourav() throws Exception {
        int databaseSizeBeforeCreate = gouravRepository.findAll().size();

        // Create the Gourav
        GouravDTO gouravDTO = gouravMapper.toDto(gourav);
        restGouravMockMvc.perform(post("/api/gouravs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gouravDTO)))
            .andExpect(status().isCreated());

        // Validate the Gourav in the database
        List<Gourav> gouravList = gouravRepository.findAll();
        assertThat(gouravList).hasSize(databaseSizeBeforeCreate + 1);
        Gourav testGourav = gouravList.get(gouravList.size() - 1);
        assertThat(testGourav.getKuchbhi()).isEqualTo(DEFAULT_KUCHBHI);
    }

    @Test
    public void createGouravWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gouravRepository.findAll().size();

        // Create the Gourav with an existing ID
        gourav.setId(UUID.randomUUID());
        GouravDTO gouravDTO = gouravMapper.toDto(gourav);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGouravMockMvc.perform(post("/api/gouravs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gouravDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Gourav> gouravList = gouravRepository.findAll();
        assertThat(gouravList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllGouravs() throws Exception {
        // Initialize the database
        gouravRepository.save(gourav);

        // Get all the gouravList
        restGouravMockMvc.perform(get("/api/gouravs"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gourav.getId().toString())))
            .andExpect(jsonPath("$.[*].kuchbhi").value(hasItem(DEFAULT_KUCHBHI.toString())));
    }

    @Test
    public void getGourav() throws Exception {
        // Initialize the database
        gouravRepository.save(gourav);

        // Get the gourav
        restGouravMockMvc.perform(get("/api/gouravs/{id}", gourav.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gourav.getId().toString()))
            .andExpect(jsonPath("$.kuchbhi").value(DEFAULT_KUCHBHI.toString()));
    }

    @Test
    public void getNonExistingGourav() throws Exception {
        // Get the gourav
        restGouravMockMvc.perform(get("/api/gouravs/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateGourav() throws Exception {
        // Initialize the database
        gouravRepository.save(gourav);
        int databaseSizeBeforeUpdate = gouravRepository.findAll().size();

        // Update the gourav
        Gourav updatedGourav = gouravRepository.findOne(gourav.getId());
        updatedGourav
            .kuchbhi(UPDATED_KUCHBHI);
        GouravDTO gouravDTO = gouravMapper.toDto(updatedGourav);

        restGouravMockMvc.perform(put("/api/gouravs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gouravDTO)))
            .andExpect(status().isOk());

        // Validate the Gourav in the database
        List<Gourav> gouravList = gouravRepository.findAll();
        assertThat(gouravList).hasSize(databaseSizeBeforeUpdate);
        Gourav testGourav = gouravList.get(gouravList.size() - 1);
        assertThat(testGourav.getKuchbhi()).isEqualTo(UPDATED_KUCHBHI);
    }

    @Test
    public void updateNonExistingGourav() throws Exception {
        int databaseSizeBeforeUpdate = gouravRepository.findAll().size();

        // Create the Gourav
        GouravDTO gouravDTO = gouravMapper.toDto(gourav);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGouravMockMvc.perform(put("/api/gouravs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gouravDTO)))
            .andExpect(status().isCreated());

        // Validate the Gourav in the database
        List<Gourav> gouravList = gouravRepository.findAll();
        assertThat(gouravList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteGourav() throws Exception {
        // Initialize the database
        gouravRepository.save(gourav);
        int databaseSizeBeforeDelete = gouravRepository.findAll().size();

        // Get the gourav
        restGouravMockMvc.perform(delete("/api/gouravs/{id}", gourav.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Gourav> gouravList = gouravRepository.findAll();
        assertThat(gouravList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gourav.class);
        Gourav gourav1 = new Gourav();
        gourav1.setId(UUID.randomUUID());
        Gourav gourav2 = new Gourav();
        gourav2.setId(gourav1.getId());
        assertThat(gourav1).isEqualTo(gourav2);
        gourav2.setId(UUID.randomUUID());
        assertThat(gourav1).isNotEqualTo(gourav2);
        gourav1.setId(null);
        assertThat(gourav1).isNotEqualTo(gourav2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GouravDTO.class);
        GouravDTO gouravDTO1 = new GouravDTO();
        gouravDTO1.setId(UUID.randomUUID());
        GouravDTO gouravDTO2 = new GouravDTO();
        assertThat(gouravDTO1).isNotEqualTo(gouravDTO2);
        gouravDTO2.setId(gouravDTO1.getId());
        assertThat(gouravDTO1).isEqualTo(gouravDTO2);
        gouravDTO2.setId(UUID.randomUUID());
        assertThat(gouravDTO1).isNotEqualTo(gouravDTO2);
        gouravDTO1.setId(null);
        assertThat(gouravDTO1).isNotEqualTo(gouravDTO2);
    }
}
