package com.sigeg.controller;

import com.sigeg.dto.request.AtualizarStatusPedidoRequest;
import com.sigeg.dto.request.PedidoRequest;
import com.sigeg.dto.response.PedidoResponse;
import com.sigeg.service.PedidoService;
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
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Sprint 3/4 – Épicos 1 e 7")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar pedido (Épico 1)")
    public PedidoResponse criar(@Valid @RequestBody PedidoRequest request, Authentication auth) {
        return pedidoService.criar(request, auth.getName());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID")
    public PedidoResponse buscarPorId(@PathVariable UUID id) {
        return pedidoService.buscarPorId(id);
    }

    @GetMapping("/meus")
    @Operation(summary = "Listar pedidos do cliente logado (Épico 2 – histórico)")
    public List<PedidoResponse> meusPedidos(Authentication auth) {
        return pedidoService.listarPorCliente(auth.getName());
    }

    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Listar pedidos de um restaurante (Épico 7)")
    public List<PedidoResponse> porRestaurante(@PathVariable UUID restauranteId, Authentication auth) {
        return pedidoService.listarPorRestaurante(restauranteId, auth.getName());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido (Épico 7 – painel restaurante)")
    public PedidoResponse atualizarStatus(@PathVariable UUID id,
                                           @Valid @RequestBody AtualizarStatusPedidoRequest request,
                                           Authentication auth) {
        return pedidoService.atualizarStatus(id, request.getStatus(), auth.getName());
    }
}
