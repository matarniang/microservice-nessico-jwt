package com.smc.smo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.smc.smo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandeNessicoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeNessico.class);
        DemandeNessico demandeNessico1 = new DemandeNessico();
        demandeNessico1.setId(1L);
        DemandeNessico demandeNessico2 = new DemandeNessico();
        demandeNessico2.setId(demandeNessico1.getId());
        assertThat(demandeNessico1).isEqualTo(demandeNessico2);
        demandeNessico2.setId(2L);
        assertThat(demandeNessico1).isNotEqualTo(demandeNessico2);
        demandeNessico1.setId(null);
        assertThat(demandeNessico1).isNotEqualTo(demandeNessico2);
    }
}
