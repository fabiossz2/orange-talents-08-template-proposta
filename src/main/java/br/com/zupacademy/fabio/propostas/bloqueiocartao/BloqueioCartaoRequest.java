package br.com.zupacademy.fabio.propostas.bloqueiocartao;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;

public class BloqueioCartaoRequest {

    @NotBlank
    private String sistemaResponsavel;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BloqueioCartaoRequest(@NotBlank String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}
