package com.sigeg.controller;

import com.sigeg.dto.request.ProdutoRequest;
import com.sigeg.dto.response.ProdutoResponse;
import com.sigeg.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Sprint 2 – Épico 6 – Cardápio")
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping("/api/restaurantes/{restauranteId}/produtos")
    @Operation(summary = "Listar produtos de um restaurante")
    public List<ProdutoResponse> listar(@PathVariable UUID restauranteId) {
        return produtoService.listarPorRestaurante(restauranteId);
    }

    @GetMapping("/api/produtos/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ProdutoResponse buscarPorId(@PathVariable UUID id) {
        return produtoService.buscarPorId(id);
    }

    @PostMapping("/api/restaurantes/{restauranteId}/produtos")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Criar produto no cardápio")
    public ProdutoResponse criar(@PathVariable UUID restauranteId,
                                  @Valid @RequestBody ProdutoRequest request,
                                  Authentication auth) {
        return produtoService.criar(restauranteId, request, auth.getName());
    }

    @PutMapping("/api/produtos/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar produto")
    public ProdutoResponse atualizar(@PathVariable UUID id,
                                      @Valid @RequestBody ProdutoRequest request,
                                      Authentication auth) {
        return produtoService.atualizar(id, request, auth.getName());
    }

    @DeleteMapping("/api/produtos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Desativar produto (soft delete)")
    public void deletar(@PathVariable UUID id, Authentication auth) {
        produtoService.deletar(id, auth.getName());
    }
}
