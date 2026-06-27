package com.sigeg.repository;

import com.sigeg.domain.model.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ChamadoRepository extends JpaRepository<Chamado, UUID> {
    List<Chamado> findByUsuarioId(UUID usuarioId);
    List<Chamado> findAllByOrderByCriadoEmDesc();
}
