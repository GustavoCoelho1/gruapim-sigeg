package com.sigeg.repository;

import com.sigeg.domain.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {
    List<Endereco> findByUsuarioId(UUID usuarioId);
}
