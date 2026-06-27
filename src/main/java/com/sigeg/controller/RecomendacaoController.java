package com.sigeg.controller;

import com.sigeg.dto.response.RecomendacaoResponse;
import com.sigeg.service.RecomendacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recomendacoes")
@RequiredArgsConstructor
@Tag(name = "Recomendações", description = "Sprint 8 – Épico 2")
@SecurityRequirement(name = "bearerAuth")
public class RecomendacaoController {

    private final RecomendacaoService recomendacaoService;

    @GetMapping
    @Operation(summary = "Obter recomendações personalizadas para o cliente logado")
    public RecomendacaoResponse recomendar(Authentication auth) {
        return recomendacaoService.recomendar(auth.getName());
    }
}
