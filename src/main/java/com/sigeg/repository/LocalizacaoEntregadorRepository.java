package com.sigeg.repository;

import com.sigeg.domain.model.LocalizacaoEntregador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface LocalizacaoEntregadorRepository extends JpaRepository<LocalizacaoEntregador, UUID> {
    List<LocalizacaoEntregador> findByEntregaIdOrderByRegistradoEmDesc(UUID entregaId);
}
