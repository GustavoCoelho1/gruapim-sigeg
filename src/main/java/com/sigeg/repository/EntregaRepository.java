package com.sigeg.repository;

import com.sigeg.domain.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntregaRepository extends JpaRepository<Entrega, UUID> {
    Optional<Entrega> findByPedidoId(UUID pedidoId);
    List<Entrega> findByEntregadorId(UUID entregadorId);
}
