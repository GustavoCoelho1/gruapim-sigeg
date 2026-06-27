package com.sigeg.repository;

import com.sigeg.domain.model.Entregador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EntregadorRepository extends JpaRepository<Entregador, UUID> {
    List<Entregador> findByDisponivelTrue();
}
