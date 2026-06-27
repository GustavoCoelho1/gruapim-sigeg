package com.sigeg.repository;

import com.sigeg.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    List<Produto> findByRestauranteId(UUID restauranteId);
    List<Produto> findByRestauranteIdAndDisponivelTrue(UUID restauranteId);
    List<Produto> findByRestauranteIdOrderByTotalVendidoDesc(UUID restauranteId);
}
