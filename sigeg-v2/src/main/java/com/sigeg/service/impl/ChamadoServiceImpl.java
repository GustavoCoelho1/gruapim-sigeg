package com.sigeg.service.impl;

import com.sigeg.domain.enums.StatusChamado;
import com.sigeg.domain.model.Chamado;
import com.sigeg.domain.model.Pedido;
import com.sigeg.domain.model.Usuario;
import com.sigeg.dto.request.ChamadoRequest;
import com.sigeg.dto.response.ChamadoResponse;
import com.sigeg.exceptions.ResourceNotFoundException;
import com.sigeg.repository.ChamadoRepository;
import com.sigeg.repository.PedidoRepository;
import com.sigeg.repository.UsuarioRepository;
import com.sigeg.service.ChamadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChamadoServiceImpl implements ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;

    @Override
    @Transactional
    public ChamadoResponse abrir(ChamadoRequest request, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Pedido pedido = null;
        if (request.getPedidoId() != null) {
            pedido = pedidoRepository.findById(request.getPedidoId()).orElse(null);
        }

        Chamado chamado = Chamado.builder()
                .usuario(usuario)
                .pedido(pedido)
                .categoria(request.getCategoria())
                .status(StatusChamado.ABERTO)
                .titulo(request.getTitulo())
                .descricao(request.getDescricao())
                .build();

        return toResponse(chamadoRepository.save(chamado));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChamadoResponse> listarMeus(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        return chamadoRepository.findByUsuarioId(usuario.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChamadoResponse> listarTodos() {
        return chamadoRepository.findAllByOrderByCriadoEmDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChamadoResponse atualizarStatus(UUID id, StatusChamado status, String resposta) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado", id));
        chamado.setStatus(status);
        if (resposta != null && !resposta.isBlank()) {
            chamado.setResposta(resposta);
        }
        return toResponse(chamadoRepository.save(chamado));
    }

    private ChamadoResponse toResponse(Chamado c) {
        return ChamadoResponse.builder()
                .id(c.getId())
                .usuarioId(c.getUsuario().getId())
                .nomeUsuario(c.getUsuario().getNome())
                .pedidoId(c.getPedido() != null ? c.getPedido().getId() : null)
                .categoria(c.getCategoria().name())
                .status(c.getStatus().name())
                .titulo(c.getTitulo())
                .descricao(c.getDescricao())
                .resposta(c.getResposta())
                .criadoEm(c.getCriadoEm())
                .atualizadoEm(c.getAtualizadoEm())
                .build();
    }
}
