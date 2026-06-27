package com.sigeg.controller;

import com.sigeg.domain.enums.StatusChamado;
import com.sigeg.dto.request.ChamadoRequest;
import com.sigeg.dto.response.ChamadoResponse;
import com.sigeg.service.ChamadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/chamados")
@RequiredArgsConstructor
@Tag(name = "Suporte", description = "Sprint 6 – Épico 5")
@SecurityRequirement(name = "bearerAuth")
public class ChamadoController {

    private final ChamadoService chamadoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Abrir chamado de suporte")
    public ChamadoResponse abrir(@Valid @RequestBody ChamadoRequest request, Authentication auth) {
        return chamadoService.abrir(request, auth.getName());
    }

    @GetMapping("/meus")
    @Operation(summary = "Listar meus chamados")
    public List<ChamadoResponse> meusChamados(Authentication auth) {
        return chamadoService.listarMeus(auth.getName());
    }

    @GetMapping
    @Operation(summary = "Listar todos os chamados (admin)")
    public List<ChamadoResponse> listarTodos() {
        return chamadoService.listarTodos();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status e resposta de um chamado (admin)")
    public ChamadoResponse atualizarStatus(@PathVariable UUID id,
                                            @RequestBody Map<String, String> body) {
        StatusChamado status = StatusChamado.valueOf(body.get("status"));
        return chamadoService.atualizarStatus(id, status, body.get("resposta"));
    }
}
