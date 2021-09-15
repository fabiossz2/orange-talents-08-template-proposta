package br.com.zupacademy.fabio.propostas.associacartaoproposta;

import br.com.zupacademy.fabio.propostas.novaproposta.Proposta;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Cartao {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank
    @Column(nullable = false)
//    @CreditCardNumber
    private String numero;

    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false)
    private Proposta proposta;

    @Deprecated
    protected Cartao() {
    }

    public Cartao(@NotBlank String numero, @NotNull Proposta proposta) {
        this.numero = numero;
        this.proposta = proposta;
    }
}
