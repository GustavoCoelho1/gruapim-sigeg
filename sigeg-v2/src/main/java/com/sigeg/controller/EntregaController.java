package com.sigeg.controller;

import com.sigeg.dto.request.LocalizacaoRequest;
import com.sigeg.dto.response.EntregaResponse;
import com.sigeg.service.EntregaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.sigeg.service.impl.EntregaServiceImpl;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/entregas")
@RequiredArgsConstructor
@Tag(name = "Rastreamento", description = "Sprint 5 – Épicos 3, 9 e 10")
@SecurityRequirement(name = "bearerAuth")
public class EntregaController {

    private final EntregaService entregaService;

    @PostMapping("/pedidos/{pedidoId}/entregador/{entregadorId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Atribuir entregador a um pedido (Épico 9/10)")
    public EntregaResponse atribuir(@PathVariable UUID pedidoId,
                                     @PathVariable UUID entregadorId,
                                     Authentication auth) {
        return entregaService.atribuirEntregador(pedidoId, entregadorId, auth.getName());
    }

    @GetMapping("/pedidos/{pedidoId}")
    @Operation(summary = "Rastrear entrega por pedido (Épico 3)")
    public EntregaResponse rastrearPorPedido(@PathVariable UUID pedidoId) {
        return entregaService.buscarPorPedido(pedidoId);
    }

    @PatchMapping("/{entregaId}/localizacao")
    @Operation(summary = "Entregador atualiza localização (Épico 3)")
    public EntregaResponse atualizarLocalizacao(@PathVariable UUID entregaId,
                                                 @Valid @RequestBody LocalizacaoRequest request,
                                                 Authentication auth) {
        return entregaService.atualizarLocalizacao(entregaId, request, auth.getName());
    }
    // agora chegou miinha vez
    @GetMapping("/disponiveis")
    @Operation(summary = "Listar os pedidos para aceitar")
    public List<EntregaResponse> listarDisponiveis(){
        return entregaService.listarEntregasDisponiveis();
    }
    @GetMapping("/demanda")
    @Operation(summary = "Regiões com mais pedidos")
    public List<RegiaoDemandaResponse> listarRegioesMaiorDemanda(){
        return entregaService.calcularRegioesMaiorDemanda();
    }
    @PostMapping("/{id}aceitar")
    @Operation(summary = "Listar as entregas aceitas")
    //sei la o que é UUID, só segui o bonde
    public EntregaResponse aceitarEntrega(@PathVariable UUID id Authentication auth){
        return entregaService.aceitarEntrega(id, auth.getName());
    }
    @GetMapping("/roteirizadas")
    @Operation(summary = "Listas as entregas, e roteirizar por distancia")
    public List<EntregaResponse> listarRoteirizadas( Authentication auth){
        return entregaService.listarEntregasAceidasOrdenadas(auth.getName());
    }
}
