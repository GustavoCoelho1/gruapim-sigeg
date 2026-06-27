package com.sigeg.controller;

import com.sigeg.dto.request.AvaliacaoRequest;
import com.sigeg.dto.response.AvaliacaoResponse;
import com.sigeg.service.AvaliacaoService;
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
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
@Tag(name = "Avaliações", description = "Sprint 6 – Épico 4")
@SecurityRequirement(name = "bearerAuth")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Avaliar pedido entregue")
    public AvaliacaoResponse avaliar(@Valid @RequestBody AvaliacaoRequest request, Authentication auth) {
        return avaliacaoService.avaliar(request, auth.getName());
    }

    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Listar avaliações de um restaurante")
    public List<AvaliacaoResponse> porRestaurante(@PathVariable UUID restauranteId) {
        return avaliacaoService.listarPorRestaurante(restauranteId);
    }

    @GetMapping("/restaurante/{restauranteId}/media")
    @Operation(summary = "Média de avaliação de um restaurante")
    public Double media(@PathVariable UUID restauranteId) {
        return avaliacaoService.mediaRestaurante(restauranteId);
    }
}
