package com.sigeg.repository;

import com.sigeg.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestauranteRepository extends JpaRepository<Restaurante, UUID> {
    List<Restaurante> findByAtivoTrue();
    Optional<Restaurante> findByUsuarioId(UUID usuarioId);

    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true AND " +
           "(LOWER(r.nomeFantasia) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(r.cidade) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Restaurante> search(@Param("q") String q);
}
