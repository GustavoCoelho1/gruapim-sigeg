package com.sigeg.controller;

import com.sigeg.dto.request.RestauranteRequest;
import com.sigeg.dto.response.RestauranteResponse;
import com.sigeg.service.RestauranteService;
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
@RequestMapping("/api/restaurantes")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "Sprint 2 – Épico 6")
public class RestauranteController {

    private final RestauranteService restauranteService;

    @GetMapping
    @Operation(summary = "Listar restaurantes ativos")
    public List<RestauranteResponse> listar() {
        return restauranteService.listarAtivos();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar restaurantes por nome ou cidade")
    public List<RestauranteResponse> buscar(@RequestParam String q) {
        return restauranteService.buscar(q);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID")
    public RestauranteResponse buscarPorId(@PathVariable UUID id) {
        return restauranteService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Cadastrar restaurante")
    public RestauranteResponse criar(@Valid @RequestBody RestauranteRequest request, Authentication auth) {
        return restauranteService.criar(request, auth.getName());
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar restaurante")
    public RestauranteResponse atualizar(@PathVariable UUID id,
                                          @Valid @RequestBody RestauranteRequest request,
                                          Authentication auth) {
        return restauranteService.atualizar(id, request, auth.getName());
    }
}
