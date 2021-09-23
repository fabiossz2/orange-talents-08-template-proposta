package br.com.zupacademy.fabio.propostas.bloqueiocartao;

import br.com.zupacademy.fabio.propostas.associacartaoproposta.Cartao;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class BloqueioCartao {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(nullable = false)
    private LocalDateTime dataHoraBloqueio;

    @NotBlank
    @Column(nullable = false)
    private String ipCliente;

    @NotBlank
    @Column(nullable = false)
    private String userAgentClient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private StatusCartao status;

    @ManyToOne
    @NotNull
    private Cartao cartao;

    @Deprecated
    protected BloqueioCartao() {
    }

    public BloqueioCartao(@NotBlank String ipCliente, @NotBlank String userAgentClient, @NotNull StatusCartao status, @NotNull Cartao cartao) {
        this.ipCliente = ipCliente;
        this.userAgentClient = userAgentClient;
        this.status = status;
        this.dataHoraBloqueio = LocalDateTime.now();
        this.cartao = cartao;
    }
}
