package com.sigeg.controller;

import com.sigeg.dto.request.EnderecoRequest;
import com.sigeg.dto.response.EnderecoResponse;
import com.sigeg.service.EnderecoService;
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
@RequestMapping("/api/enderecos")
@RequiredArgsConstructor
@Tag(name = "Endereços", description = "Gerenciamento de endereços do cliente")
@SecurityRequirement(name = "bearerAuth")
public class EnderecoController {

    private final EnderecoService enderecoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adicionar endereço")
    public EnderecoResponse criar(@Valid @RequestBody EnderecoRequest request, Authentication auth) {
        return enderecoService.criar(request, auth.getName());
    }

    @GetMapping
    @Operation(summary = "Listar meus endereços")
    public List<EnderecoResponse> listar(Authentication auth) {
        return enderecoService.listarMeus(auth.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover endereço")
    public void deletar(@PathVariable UUID id, Authentication auth) {
        enderecoService.deletar(id, auth.getName());
    }
}
