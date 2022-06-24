package com.smc.smo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.smc.smo.IntegrationTest;
import com.smc.smo.domain.DemandeNessico;
import com.smc.smo.repository.DemandeNessicoRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DemandeNessicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DemandeNessicoResourceIT {

    private static final String DEFAULT_NOM_APP = "AAAAAAAAAA";
    private static final String UPDATED_NOM_APP = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEMANDE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEMANDE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_RETOUR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RETOUR = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/demande-nessicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandeNessicoRepository demandeNessicoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeNessicoMockMvc;

    private DemandeNessico demandeNessico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeNessico createEntity(EntityManager em) {
        DemandeNessico demandeNessico = new DemandeNessico()
            .nomApp(DEFAULT_NOM_APP)
            .password(DEFAULT_PASSWORD)
            .action(DEFAULT_ACTION)
            .status(DEFAULT_STATUS)
            .message(DEFAULT_MESSAGE)
            .dateDemande(DEFAULT_DATE_DEMANDE)
            .dateRetour(DEFAULT_DATE_RETOUR)
            .user(DEFAULT_USER);
        return demandeNessico;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeNessico createUpdatedEntity(EntityManager em) {
        DemandeNessico demandeNessico = new DemandeNessico()
            .nomApp(UPDATED_NOM_APP)
            .password(UPDATED_PASSWORD)
            .action(UPDATED_ACTION)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .dateRetour(UPDATED_DATE_RETOUR)
            .user(UPDATED_USER);
        return demandeNessico;
    }

    @BeforeEach
    public void initTest() {
        demandeNessico = createEntity(em);
    }

    @Test
    @Transactional
    void createDemandeNessico() throws Exception {
        int databaseSizeBeforeCreate = demandeNessicoRepository.findAll().size();
        // Create the DemandeNessico
        restDemandeNessicoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeNessico))
            )
            .andExpect(status().isCreated());

        // Validate the DemandeNessico in the database
        List<DemandeNessico> demandeNessicoList = demandeNessicoRepository.findAll();
        assertThat(demandeNessicoList).hasSize(databaseSizeBeforeCreate + 1);
        DemandeNessico testDemandeNessico = demandeNessicoList.get(demandeNessicoList.size() - 1);
        assertThat(testDemandeNessico.getNomApp()).isEqualTo(DEFAULT_NOM_APP);
        assertThat(testDemandeNessico.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testDemandeNessico.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testDemandeNessico.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDemandeNessico.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testDemandeNessico.getDateDemande()).isEqualTo(DEFAULT_DATE_DEMANDE);
        assertThat(testDemandeNessico.getDateRetour()).isEqualTo(DEFAULT_DATE_RETOUR);
        assertThat(testDemandeNessico.getUser()).isEqualTo(DEFAULT_USER);
    }

    @Test
    @Transactional
    void createDemandeNessicoWithExistingId() throws Exception {
        // Create the DemandeNessico with an existing ID
        demandeNessico.setId(1L);

        int databaseSizeBeforeCreate = demandeNessicoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeNessicoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeNessico))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeNessico in the database
        List<DemandeNessico> demandeNessicoList = demandeNessicoRepository.findAll();
        assertThat(demandeNessicoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDemandeNessicos() throws Exception {
        // Initialize the database
        demandeNessicoRepository.saveAndFlush(demandeNessico);

        // Get all the demandeNessicoList
        restDemandeNessicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeNessico.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomApp").value(hasItem(DEFAULT_NOM_APP)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].dateDemande").value(hasItem(DEFAULT_DATE_DEMANDE.toString())))
            .andExpect(jsonPath("$.[*].dateRetour").value(hasItem(DEFAULT_DATE_RETOUR.toString())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER)));
    }

    @Test
    @Transactional
    void getDemandeNessico() throws Exception {
        // Initialize the database
        demandeNessicoRepository.saveAndFlush(demandeNessico);

        // Get the demandeNessico
        restDemandeNessicoMockMvc
            .perform(get(ENTITY_API_URL_ID, demandeNessico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demandeNessico.getId().intValue()))
            .andExpect(jsonPath("$.nomApp").value(DEFAULT_NOM_APP))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.dateDemande").value(DEFAULT_DATE_DEMANDE.toString()))
            .andExpect(jsonPath("$.dateRetour").value(DEFAULT_DATE_RETOUR.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER));
    }

    @Test
    @Transactional
    void getNonExistingDemandeNessico() throws Exception {
        // Get the demandeNessico
        restDemandeNessicoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemandeNessico() throws Exception {
        // Initialize the database
        demandeNessicoRepository.saveAndFlush(demandeNessico);

        int databaseSizeBeforeUpdate = demandeNessicoRepository.findAll().size();

        // Update the demandeNessico
        DemandeNessico updatedDemandeNessico = demandeNessicoRepository.findById(demandeNessico.getId()).get();
        // Disconnect from session so that the updates on updatedDemandeNessico are not directly saved in db
        em.detach(updatedDemandeNessico);
        updatedDemandeNessico
            .nomApp(UPDATED_NOM_APP)
            .password(UPDATED_PASSWORD)
            .action(UPDATED_ACTION)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .dateRetour(UPDATED_DATE_RETOUR)
            .user(UPDATED_USER);

        restDemandeNessicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDemandeNessico.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDemandeNessico))
            )
            .andExpect(status().isOk());

        // Validate the DemandeNessico in the database
        List<DemandeNessico> demandeNessicoList = demandeNessicoRepository.findAll();
        assertThat(demandeNessicoList).hasSize(databaseSizeBeforeUpdate);
        DemandeNessico testDemandeNessico = demandeNessicoList.get(demandeNessicoList.size() - 1);
        assertThat(testDemandeNessico.getNomApp()).isEqualTo(UPDATED_NOM_APP);
        assertThat(testDemandeNessico.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testDemandeNessico.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testDemandeNessico.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDemandeNessico.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testDemandeNessico.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testDemandeNessico.getDateRetour()).isEqualTo(UPDATED_DATE_RETOUR);
        assertThat(testDemandeNessico.getUser()).isEqualTo(UPDATED_USER);
    }

    @Test
    @Transactional
    void putNonExistingDemandeNessico() throws Exception {
        int databaseSizeBeforeUpdate = demandeNessicoRepository.findAll().size();
        demandeNessico.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeNessicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeNessico.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeNessico))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeNessico in the database
        List<DemandeNessico> demandeNessicoList = demandeNessicoRepository.findAll();
        assertThat(demandeNessicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemandeNessico() throws Exception {
        int databaseSizeBeforeUpdate = demandeNessicoRepository.findAll().size();
        demandeNessico.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeNessicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeNessico))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeNessico in the database
        List<DemandeNessico> demandeNessicoList = demandeNessicoRepository.findAll();
        assertThat(demandeNessicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemandeNessico() throws Exception {
        int databaseSizeBeforeUpdate = demandeNessicoRepository.findAll().size();
        demandeNessico.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeNessicoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeNessico))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeNessico in the database
        List<DemandeNessico> demandeNessicoList = demandeNessicoRepository.findAll();
        assertThat(demandeNessicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeNessicoWithPatch() throws Exception {
        // Initialize the database
        demandeNessicoRepository.saveAndFlush(demandeNessico);

        int databaseSizeBeforeUpdate = demandeNessicoRepository.findAll().size();

        // Update the demandeNessico using partial update
        DemandeNessico partialUpdatedDemandeNessico = new DemandeNessico();
        partialUpdatedDemandeNessico.setId(demandeNessico.getId());

        partialUpdatedDemandeNessico
            .nomApp(UPDATED_NOM_APP)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .dateRetour(UPDATED_DATE_RETOUR)
            .user(UPDATED_USER);

        restDemandeNessicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeNessico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeNessico))
            )
            .andExpect(status().isOk());

        // Validate the DemandeNessico in the database
        List<DemandeNessico> demandeNessicoList = demandeNessicoRepository.findAll();
        assertThat(demandeNessicoList).hasSize(databaseSizeBeforeUpdate);
        DemandeNessico testDemandeNessico = demandeNessicoList.get(demandeNessicoList.size() - 1);
        assertThat(testDemandeNessico.getNomApp()).isEqualTo(UPDATED_NOM_APP);
        assertThat(testDemandeNessico.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testDemandeNessico.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testDemandeNessico.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDemandeNessico.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testDemandeNessico.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testDemandeNessico.getDateRetour()).isEqualTo(UPDATED_DATE_RETOUR);
        assertThat(testDemandeNessico.getUser()).isEqualTo(UPDATED_USER);
    }

    @Test
    @Transactional
    void fullUpdateDemandeNessicoWithPatch() throws Exception {
        // Initialize the database
        demandeNessicoRepository.saveAndFlush(demandeNessico);

        int databaseSizeBeforeUpdate = demandeNessicoRepository.findAll().size();

        // Update the demandeNessico using partial update
        DemandeNessico partialUpdatedDemandeNessico = new DemandeNessico();
        partialUpdatedDemandeNessico.setId(demandeNessico.getId());

        partialUpdatedDemandeNessico
            .nomApp(UPDATED_NOM_APP)
            .password(UPDATED_PASSWORD)
            .action(UPDATED_ACTION)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .dateDemande(UPDATED_DATE_DEMANDE)
            .dateRetour(UPDATED_DATE_RETOUR)
            .user(UPDATED_USER);

        restDemandeNessicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeNessico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeNessico))
            )
            .andExpect(status().isOk());

        // Validate the DemandeNessico in the database
        List<DemandeNessico> demandeNessicoList = demandeNessicoRepository.findAll();
        assertThat(demandeNessicoList).hasSize(databaseSizeBeforeUpdate);
        DemandeNessico testDemandeNessico = demandeNessicoList.get(demandeNessicoList.size() - 1);
        assertThat(testDemandeNessico.getNomApp()).isEqualTo(UPDATED_NOM_APP);
        assertThat(testDemandeNessico.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testDemandeNessico.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testDemandeNessico.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDemandeNessico.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testDemandeNessico.getDateDemande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testDemandeNessico.getDateRetour()).isEqualTo(UPDATED_DATE_RETOUR);
        assertThat(testDemandeNessico.getUser()).isEqualTo(UPDATED_USER);
    }

    @Test
    @Transactional
    void patchNonExistingDemandeNessico() throws Exception {
        int databaseSizeBeforeUpdate = demandeNessicoRepository.findAll().size();
        demandeNessico.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeNessicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandeNessico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeNessico))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeNessico in the database
        List<DemandeNessico> demandeNessicoList = demandeNessicoRepository.findAll();
        assertThat(demandeNessicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemandeNessico() throws Exception {
        int databaseSizeBeforeUpdate = demandeNessicoRepository.findAll().size();
        demandeNessico.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeNessicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeNessico))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeNessico in the database
        List<DemandeNessico> demandeNessicoList = demandeNessicoRepository.findAll();
        assertThat(demandeNessicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemandeNessico() throws Exception {
        int databaseSizeBeforeUpdate = demandeNessicoRepository.findAll().size();
        demandeNessico.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeNessicoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeNessico))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeNessico in the database
        List<DemandeNessico> demandeNessicoList = demandeNessicoRepository.findAll();
        assertThat(demandeNessicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemandeNessico() throws Exception {
        // Initialize the database
        demandeNessicoRepository.saveAndFlush(demandeNessico);

        int databaseSizeBeforeDelete = demandeNessicoRepository.findAll().size();

        // Delete the demandeNessico
        restDemandeNessicoMockMvc
            .perform(delete(ENTITY_API_URL_ID, demandeNessico.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemandeNessico> demandeNessicoList = demandeNessicoRepository.findAll();
        assertThat(demandeNessicoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
