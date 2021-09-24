package br.com.zupacademy.fabio.propostas.novaproposta;

import br.com.zupacademy.fabio.propostas.validators.CpfOrCnpjValidator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class NovaPropostaPostRequest {

    @CpfOrCnpjValidator
    @NotBlank
    private String documento;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String nome;
    @NotBlank
    private String endereco;
    @Positive
    @NotNull
    private BigDecimal salario;

    public NovaPropostaPostRequest(@CpfOrCnpjValidator @NotBlank String documento, @Email @NotBlank String email,
                                   @NotBlank String nome, @NotBlank String endereco, @Positive @NotNull BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Proposta toModel() {
        return new Proposta(documento, email, nome, endereco, salario);
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }
}
