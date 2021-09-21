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
    private String ipCliente;

    @NotBlank
    private String userAgentClient;

    @Enumerated(EnumType.STRING)
    private StatusCartao status;

    @ManyToOne
    private Cartao cartao;

    @Deprecated
    protected BloqueioCartao() {
    }

    public BloqueioCartao(@NotBlank String ipCliente, @NotBlank String userAgentClient, StatusCartao status, @NotNull Cartao cartao) {
        this.ipCliente = ipCliente;
        this.userAgentClient = userAgentClient;
        this.status = status;
        this.dataHoraBloqueio = LocalDateTime.now();
        this.cartao = cartao;
    }
}
