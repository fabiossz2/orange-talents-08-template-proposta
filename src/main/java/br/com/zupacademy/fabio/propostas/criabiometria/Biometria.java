package br.com.zupacademy.fabio.propostas.criabiometria;

import br.com.zupacademy.fabio.propostas.associacartaoproposta.Cartao;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String fingerprint;

    @Column(nullable = false)
    private LocalDateTime dataAssociacao;

    @ManyToOne
    @NotNull
    private Cartao cartao;

    @Deprecated
    protected Biometria() {
    }

    public Biometria(@NotBlank String fingerprint, @NotNull Cartao cartao) {
        this.fingerprint = fingerprint;
        this.cartao = cartao;
        this.dataAssociacao = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
