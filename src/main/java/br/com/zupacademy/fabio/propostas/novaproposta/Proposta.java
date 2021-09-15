package br.com.zupacademy.fabio.propostas.novaproposta;

import br.com.zupacademy.fabio.propostas.validators.CpfOrCnpjValidator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @CpfOrCnpjValidator
    @NotBlank
    @Column(unique = true, nullable = false)
    private String documento;
    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;
    @NotBlank
    @Column(nullable = false)
    private String nome;
    @NotBlank
    @Column(nullable = false)
    private String endereco;
    @Positive
    @NotNull
    private BigDecimal salario;
    @Enumerated(EnumType.STRING)
    private PropostaStatus status;

    @Deprecated
    protected Proposta() {
    }

    public Proposta(@CpfOrCnpjValidator @NotBlank String documento, @Email @NotBlank String email, @NotBlank String nome,
                    @NotBlank String endereco, @Positive @NotNull BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
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

    public PropostaStatus getStatus() {
        return status;
    }

    public Map<String, String> criaSolicitacaoAnalise() {
        Map<String, String> map = new HashMap<>();
        map.put("idProposta", this.id.toString());
        map.put("nome", this.nome);
        map.put("documento", this.documento);
        return map;
    }

    public void setStatus(PropostaStatus status) {
        this.status = status;
    }
}
