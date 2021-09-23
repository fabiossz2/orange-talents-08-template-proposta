package br.com.zupacademy.fabio.propostas.acompanhamentoproposta;

import br.com.zupacademy.fabio.propostas.novaproposta.Proposta;

import java.math.BigDecimal;

public class PropostaDto {
    private String id;
    private BigDecimal salario;
    private String statusProposta;

    public PropostaDto(Proposta proposta) {
        this.id = proposta.getId();
        this.salario = proposta.getSalario();
        this.statusProposta = proposta.getStatus().toString();
    }

    public String getId() {
        return id;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public String getStatusProposta() {
        return statusProposta;
    }
}
