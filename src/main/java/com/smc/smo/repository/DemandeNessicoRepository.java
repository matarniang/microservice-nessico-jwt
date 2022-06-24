package com.smc.smo.repository;

import com.smc.smo.domain.DemandeNessico;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data SQL repository for the DemandeNessico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemandeNessicoRepository extends JpaRepository<DemandeNessico, Long> {
    @Query(value="SELECT * FROM demande_nessico p WHERE user=?1",nativeQuery =true)
    List<DemandeNessico> GetDemande(String login);
}
