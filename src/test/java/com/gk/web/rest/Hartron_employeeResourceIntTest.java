package com.gk.web.rest;

import com.gk.AbstractCassandraTest;
import com.gk.RepoApp;

import com.gk.domain.Hartron_employee;
import com.gk.repository.Hartron_employeeRepository;
import com.gk.service.Hartron_employeeService;
import com.gk.service.dto.Hartron_employeeDTO;
import com.gk.service.mapper.Hartron_employeeMapper;
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
 * Test class for the Hartron_employeeResource REST controller.
 *
 * @see Hartron_employeeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepoApp.class)
public class Hartron_employeeResourceIntTest extends AbstractCassandraTest {

    @Autowired
    private Hartron_employeeRepository hartron_employeeRepository;

    @Autowired
    private Hartron_employeeMapper hartron_employeeMapper;

    @Autowired
    private Hartron_employeeService hartron_employeeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restHartron_employeeMockMvc;

    private Hartron_employee hartron_employee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Hartron_employeeResource hartron_employeeResource = new Hartron_employeeResource(hartron_employeeService);
        this.restHartron_employeeMockMvc = MockMvcBuilders.standaloneSetup(hartron_employeeResource)
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
    public static Hartron_employee createEntity() {
        Hartron_employee hartron_employee = new Hartron_employee();
        return hartron_employee;
    }

    @Before
    public void initTest() {
        hartron_employeeRepository.deleteAll();
        hartron_employee = createEntity();
    }

    @Test
    public void createHartron_employee() throws Exception {
        int databaseSizeBeforeCreate = hartron_employeeRepository.findAll().size();

        // Create the Hartron_employee
        Hartron_employeeDTO hartron_employeeDTO = hartron_employeeMapper.toDto(hartron_employee);
        restHartron_employeeMockMvc.perform(post("/api/hartron-employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hartron_employeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Hartron_employee in the database
        List<Hartron_employee> hartron_employeeList = hartron_employeeRepository.findAll();
        assertThat(hartron_employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Hartron_employee testHartron_employee = hartron_employeeList.get(hartron_employeeList.size() - 1);
    }

    @Test
    public void createHartron_employeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hartron_employeeRepository.findAll().size();

        // Create the Hartron_employee with an existing ID
        hartron_employee.setId(UUID.randomUUID());
        Hartron_employeeDTO hartron_employeeDTO = hartron_employeeMapper.toDto(hartron_employee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHartron_employeeMockMvc.perform(post("/api/hartron-employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hartron_employeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Hartron_employee> hartron_employeeList = hartron_employeeRepository.findAll();
        assertThat(hartron_employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllHartron_employees() throws Exception {
        // Initialize the database
        hartron_employeeRepository.save(hartron_employee);

        // Get all the hartron_employeeList
        restHartron_employeeMockMvc.perform(get("/api/hartron-employees"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hartron_employee.getId().toString())));
    }

    @Test
    public void getHartron_employee() throws Exception {
        // Initialize the database
        hartron_employeeRepository.save(hartron_employee);

        // Get the hartron_employee
        restHartron_employeeMockMvc.perform(get("/api/hartron-employees/{id}", hartron_employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hartron_employee.getId().toString()));
    }

    @Test
    public void getNonExistingHartron_employee() throws Exception {
        // Get the hartron_employee
        restHartron_employeeMockMvc.perform(get("/api/hartron-employees/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateHartron_employee() throws Exception {
        // Initialize the database
        hartron_employeeRepository.save(hartron_employee);
        int databaseSizeBeforeUpdate = hartron_employeeRepository.findAll().size();

        // Update the hartron_employee
        Hartron_employee updatedHartron_employee = hartron_employeeRepository.findOne(hartron_employee.getId());
        Hartron_employeeDTO hartron_employeeDTO = hartron_employeeMapper.toDto(updatedHartron_employee);

        restHartron_employeeMockMvc.perform(put("/api/hartron-employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hartron_employeeDTO)))
            .andExpect(status().isOk());

        // Validate the Hartron_employee in the database
        List<Hartron_employee> hartron_employeeList = hartron_employeeRepository.findAll();
        assertThat(hartron_employeeList).hasSize(databaseSizeBeforeUpdate);
        Hartron_employee testHartron_employee = hartron_employeeList.get(hartron_employeeList.size() - 1);
    }

    @Test
    public void updateNonExistingHartron_employee() throws Exception {
        int databaseSizeBeforeUpdate = hartron_employeeRepository.findAll().size();

        // Create the Hartron_employee
        Hartron_employeeDTO hartron_employeeDTO = hartron_employeeMapper.toDto(hartron_employee);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHartron_employeeMockMvc.perform(put("/api/hartron-employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hartron_employeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Hartron_employee in the database
        List<Hartron_employee> hartron_employeeList = hartron_employeeRepository.findAll();
        assertThat(hartron_employeeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteHartron_employee() throws Exception {
        // Initialize the database
        hartron_employeeRepository.save(hartron_employee);
        int databaseSizeBeforeDelete = hartron_employeeRepository.findAll().size();

        // Get the hartron_employee
        restHartron_employeeMockMvc.perform(delete("/api/hartron-employees/{id}", hartron_employee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Hartron_employee> hartron_employeeList = hartron_employeeRepository.findAll();
        assertThat(hartron_employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hartron_employee.class);
        Hartron_employee hartron_employee1 = new Hartron_employee();
        hartron_employee1.setId(UUID.randomUUID());
        Hartron_employee hartron_employee2 = new Hartron_employee();
        hartron_employee2.setId(hartron_employee1.getId());
        assertThat(hartron_employee1).isEqualTo(hartron_employee2);
        hartron_employee2.setId(UUID.randomUUID());
        assertThat(hartron_employee1).isNotEqualTo(hartron_employee2);
        hartron_employee1.setId(null);
        assertThat(hartron_employee1).isNotEqualTo(hartron_employee2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hartron_employeeDTO.class);
        Hartron_employeeDTO hartron_employeeDTO1 = new Hartron_employeeDTO();
        hartron_employeeDTO1.setId(UUID.randomUUID());
        Hartron_employeeDTO hartron_employeeDTO2 = new Hartron_employeeDTO();
        assertThat(hartron_employeeDTO1).isNotEqualTo(hartron_employeeDTO2);
        hartron_employeeDTO2.setId(hartron_employeeDTO1.getId());
        assertThat(hartron_employeeDTO1).isEqualTo(hartron_employeeDTO2);
        hartron_employeeDTO2.setId(UUID.randomUUID());
        assertThat(hartron_employeeDTO1).isNotEqualTo(hartron_employeeDTO2);
        hartron_employeeDTO1.setId(null);
        assertThat(hartron_employeeDTO1).isNotEqualTo(hartron_employeeDTO2);
    }
}
