package br.com.zupacademy.fabio.propostas.avisoviagem;

import br.com.zupacademy.fabio.propostas.associacartaoproposta.Cartao;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
class AvisoViagem {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(nullable = false)
    private LocalDateTime instante;

    @NotBlank
    @Column(nullable = false)
    private String ipClient;

    @NotBlank
    @Column(nullable = false)
    private String userAgentClient;

    @ManyToOne
    @NotNull
    private Cartao cartao;

    @Deprecated
    protected AvisoViagem() {
    }

    AvisoViagem(@NotBlank String ipClient, @NotBlank String userAgentClient, @NotNull Cartao cartao) {
        this.instante = LocalDateTime.now();
        this.ipClient = ipClient;
        this.userAgentClient = userAgentClient;
        this.cartao = cartao;
    }
}
