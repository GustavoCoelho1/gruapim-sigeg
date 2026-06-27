package com.sigeg.dto.request;

import com.sigeg.domain.enums.TipoPerfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Size(max = 100)
    private String nome;

    @NotBlank @Email @Size(max = 100)
    private String email;

    @NotBlank @Size(min = 6, max = 100)
    private String senha;

    @NotNull
    private TipoPerfil tipoPerfil;

    // opcionais
    private String cpf;
    private String veiculo;
    private String placa;
}
