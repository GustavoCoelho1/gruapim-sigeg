package com.sigeg.service.impl;

import com.sigeg.domain.enums.TipoPerfil;
import com.sigeg.domain.model.Cliente;
import com.sigeg.domain.model.Entregador;
import com.sigeg.domain.model.Usuario;
import com.sigeg.dto.request.LoginRequest;
import com.sigeg.dto.request.RegisterRequest;
import com.sigeg.dto.response.AuthResponse;
import com.sigeg.exceptions.BusinessException;
import com.sigeg.repository.UsuarioRepository;
import com.sigeg.security.JwtService;
import com.sigeg.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("E-mail já cadastrado: " + request.getEmail());
        }

        Usuario usuario = buildUsuario(request);
        usuario = usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario.getEmail(), usuario.getTipoPerfil().name());
        return buildAuthResponse(usuario, token);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenhaHash())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        if (!usuario.isAtivo()) {
            throw new BusinessException("Usuário inativo.");
        }

        String token = jwtService.generateToken(usuario.getEmail(), usuario.getTipoPerfil().name());
        return buildAuthResponse(usuario, token);
    }

    private Usuario buildUsuario(RegisterRequest req) {
        String hash = passwordEncoder.encode(req.getSenha());
        TipoPerfil perfil = req.getTipoPerfil();

        return switch (perfil) {
            case CLIENTE -> {
                Cliente c = new Cliente();
                c.setNome(req.getNome());
                c.setEmail(req.getEmail());
                c.setSenhaHash(hash);
                c.setCpf(req.getCpf());
                yield c;
            }
            case ENTREGADOR -> {
                Entregador e = new Entregador();
                e.setNome(req.getNome());
                e.setEmail(req.getEmail());
                e.setSenhaHash(hash);
                e.setCpf(req.getCpf());
                e.setVeiculo(req.getVeiculo());
                e.setPlaca(req.getPlaca());
                yield e;
            }
            default -> {
                Usuario u = new Usuario();
                u.setNome(req.getNome());
                u.setEmail(req.getEmail());
                u.setSenhaHash(hash);
                yield u;
            }
        };
    }

    private AuthResponse buildAuthResponse(Usuario u, String token) {
        return AuthResponse.builder()
                .token(token)
                .userId(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .tipoPerfil(u.getTipoPerfil() != null ? u.getTipoPerfil().name() : null)
                .build();
    }
}
