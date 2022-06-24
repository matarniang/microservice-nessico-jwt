package com.smc.smo.web.rest;

import com.smc.smo.domain.DemandeNessico;
import com.smc.smo.repository.DemandeNessicoRepository;
import com.smc.smo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import java.time.LocalDate; 
import org.json.JSONObject;

/**
 * REST controller for managing {@link com.smc.smo.domain.DemandeNessico}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DemandeNessicoResource {

    LocalDate date = LocalDate.now();
    private final Logger log = LoggerFactory.getLogger(DemandeNessicoResource.class);

    private static final String ENTITY_NAME = "nessicoDemandeNessico";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemandeNessicoRepository demandeNessicoRepository;

    public DemandeNessicoResource(DemandeNessicoRepository demandeNessicoRepository) {
        this.demandeNessicoRepository = demandeNessicoRepository;
    }

    /**
     * {@code POST  /demande-nessicos} : Create a new demandeNessico.
     *
     * @param demandeNessico the demandeNessico to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demandeNessico, or with status {@code 400 (Bad Request)} if the demandeNessico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demande-nessicos")
    public ResponseEntity<DemandeNessico> createDemandeNessico(@RequestBody DemandeNessico demandeNessico) throws URISyntaxException {        
        demandeNessico.setDateDemande(date);
        log.debug("REST request to save DemandeNessico : {}", demandeNessico);
        if (demandeNessico.getId() != null) {
            throw new BadRequestAlertException("A new demandeNessico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandeNessico result = demandeNessicoRepository.save(demandeNessico);
        return ResponseEntity
            .created(new URI("/api/demande-nessicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demande-nessicos/:id} : Updates an existing demandeNessico.
     *
     * @param id the id of the demandeNessico to save.
     * @param demandeNessico the demandeNessico to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeNessico,
     * or with status {@code 400 (Bad Request)} if the demandeNessico is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demandeNessico couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demande-nessicos/{id}")
    public ResponseEntity<DemandeNessico> updateDemandeNessico(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DemandeNessico demandeNessico
    ) throws URISyntaxException {
        log.debug("REST request to update DemandeNessico : {}, {}", id, demandeNessico);
        if (demandeNessico.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeNessico.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeNessicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemandeNessico result = demandeNessicoRepository.save(demandeNessico);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeNessico.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demande-nessicos/:id} : Partial updates given fields of an existing demandeNessico, field will ignore if it is null
     *
     * @param id the id of the demandeNessico to save.
     * @param demandeNessico the demandeNessico to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeNessico,
     * or with status {@code 400 (Bad Request)} if the demandeNessico is not valid,
     * or with status {@code 404 (Not Found)} if the demandeNessico is not found,
     * or with status {@code 500 (Internal Server Error)} if the demandeNessico couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demande-nessicos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DemandeNessico> partialUpdateDemandeNessico(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DemandeNessico demandeNessico
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemandeNessico partially : {}, {}", id, demandeNessico);
        if (demandeNessico.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeNessico.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeNessicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemandeNessico> result = demandeNessicoRepository
            .findById(demandeNessico.getId())
            .map(existingDemandeNessico -> {
                if (demandeNessico.getNomApp() != null) {
                    existingDemandeNessico.setNomApp(demandeNessico.getNomApp());
                }
                if (demandeNessico.getPassword() != null) {
                    existingDemandeNessico.setPassword(demandeNessico.getPassword());
                }
                if (demandeNessico.getAction() != null) {
                    existingDemandeNessico.setAction(demandeNessico.getAction());
                }
                if (demandeNessico.getStatus() != null) {
                    existingDemandeNessico.setStatus(demandeNessico.getStatus());
                }
                if (demandeNessico.getMessage() != null) {
                    existingDemandeNessico.setMessage(demandeNessico.getMessage());
                }
                if (demandeNessico.getDateDemande() != null) {
                    existingDemandeNessico.setDateDemande(demandeNessico.getDateDemande());
                }
                if (demandeNessico.getDateRetour() != null) {
                    existingDemandeNessico.setDateRetour(demandeNessico.getDateRetour());
                }
                if (demandeNessico.getUser() != null) {
                    existingDemandeNessico.setUser(demandeNessico.getUser());
                }

                return existingDemandeNessico;
            })
            .map(demandeNessicoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeNessico.getId().toString())
        );
    }

    /**
     * {@code GET  /demande-nessicos} : get all the demandeNessicos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandeNessicos in body.
     */
    @GetMapping("/demande-nessicos")
    public List<DemandeNessico> getAllDemandeNessicos() {
        log.debug("REST request to get all DemandeNessicos");
        return demandeNessicoRepository.findAll();
    }

    /**
     * {@code POST  /demande-nessicos-login} : get all the demande-nessicos-login.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandes in body.
     */
    @PostMapping("/demande-nessicos-login")
    public List<DemandeNessico> getDemande(@RequestBody String request) {
        log.debug("REST request to get all Demandes");
        JSONObject object=new JSONObject(request);
        String username=object.getString("user");
        return demandeNessicoRepository.GetDemande(username);
    }

    /**
     * {@code POST  /demande-nessicos-delete} : get the demande delete.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandes in body.
     */
    @PostMapping("/demande-nessicos-delete")
    public ResponseEntity<Void> delete(@RequestBody String request) {
        JSONObject object=new JSONObject(request);
        Long id=object.getLong("id");
        log.debug("REST request to delete DemandeOracle : {}", id);
        demandeNessicoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /demande-nessicos/:id} : get the "id" demandeNessico.
     *
     * @param id the id of the demandeNessico to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demandeNessico, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demande-nessicos/{id}")
    public ResponseEntity<DemandeNessico> getDemandeNessico(@PathVariable Long id) {
        log.debug("REST request to get DemandeNessico : {}", id);
        Optional<DemandeNessico> demandeNessico = demandeNessicoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(demandeNessico);
    }

    /**
     * {@code DELETE  /demande-nessicos/:id} : delete the "id" demandeNessico.
     *
     * @param id the id of the demandeNessico to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demande-nessicos/{id}")
    public ResponseEntity<Void> deleteDemandeNessico(@PathVariable Long id) {
        log.debug("REST request to delete DemandeNessico : {}", id);
        demandeNessicoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
