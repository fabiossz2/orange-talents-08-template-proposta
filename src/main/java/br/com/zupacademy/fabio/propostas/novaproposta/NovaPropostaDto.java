package br.com.zupacademy.fabio.propostas.novaproposta;

import java.math.BigDecimal;

public class NovaPropostaDto {
    private String id;
    private String documento;
    private String email;
    private String nome;
    private String endereco;
    private BigDecimal salario;
    private String statusProposta;


    public NovaPropostaDto(Proposta proposta) {
        this.id = proposta.getId();
        this.documento = proposta.getDocumento();
        this.email = proposta.getEmail();
        this.nome = proposta.getNome();
        this.endereco = proposta.getEndereco();
        this.salario = proposta.getSalario();
        this.statusProposta = proposta.getStatus().toString();
    }

    public String getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public String getStatusProposta() {
        return statusProposta;
    }
}
