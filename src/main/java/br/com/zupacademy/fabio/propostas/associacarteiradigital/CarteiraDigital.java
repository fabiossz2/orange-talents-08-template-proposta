package br.com.zupacademy.fabio.propostas.associacarteiradigital;

import br.com.zupacademy.fabio.propostas.associacartaoproposta.Cartao;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class CarteiraDigital {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoCarteiraDigital carteiraDigital;

    @ManyToOne
    @NotNull
    private Cartao cartao;

    @Deprecated
    protected CarteiraDigital() {
    }

    public CarteiraDigital(@NotBlank @Email String email, @NotNull TipoCarteiraDigital carteiraDigital,
                           @NotNull Cartao cartao) {
        this.email = email;
        this.carteiraDigital = carteiraDigital;
        this.cartao = cartao;
    }

    public String getId() {
        return id;
    }

    public TipoCarteiraDigital getCarteiraDigital() {
        return carteiraDigital;
    }
}
