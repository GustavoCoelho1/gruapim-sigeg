package com.sigeg.repository;

import com.sigeg.domain.enums.StatusPedido;
import com.sigeg.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    List<Pedido> findByClienteIdOrderByCriadoEmDesc(UUID clienteId);
    List<Pedido> findByRestauranteIdOrderByCriadoEmDesc(UUID restauranteId);
    List<Pedido> findByRestauranteIdAndStatus(UUID restauranteId, StatusPedido status);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.restaurante.id = :rid AND p.criadoEm BETWEEN :ini AND :fim")
    Long countByRestauranteAndPeriodo(@Param("rid") UUID rid, @Param("ini") LocalDateTime ini, @Param("fim") LocalDateTime fim);

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.restaurante.id = :rid AND p.criadoEm BETWEEN :ini AND :fim AND p.status = 'ENTREGUE'")
    BigDecimal faturamentoByRestauranteAndPeriodo(@Param("rid") UUID rid, @Param("ini") LocalDateTime ini, @Param("fim") LocalDateTime fim);

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :cid ORDER BY p.criadoEm DESC")
    List<Pedido> historicoCliente(@Param("cid") UUID cid);
}
