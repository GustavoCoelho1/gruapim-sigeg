package com.sigeg.repository;

import com.sigeg.domain.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {
    Optional<Avaliacao> findByPedidoId(UUID pedidoId);
    List<Avaliacao> findByRestauranteId(UUID restauranteId);

    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.restaurante.id = :rid")
    Double mediaNotaRestaurante(@Param("rid") UUID rid);
}
