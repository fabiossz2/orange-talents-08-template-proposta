package br.com.zupacademy.fabio.propostas.associacartaoproposta;

import br.com.zupacademy.fabio.propostas.criabiometria.Biometria;
import br.com.zupacademy.fabio.propostas.novaproposta.Proposta;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Cartao {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String numero;

    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false)
    private Proposta proposta;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "cartao")
    private List<Biometria> biometrias = new ArrayList<>();

    @Deprecated
    protected Cartao() {
    }

    public Cartao(@NotBlank String numero, @NotNull Proposta proposta) {
        this.numero = numero;
        this.proposta = proposta;
    }

    public String getId() {
        return id;
    }

    public void adicionaBiometria(Biometria biometria) {
        this.biometrias.add(biometria);
    }

    public List<Biometria> getBiometrias() {
        return Collections.unmodifiableList(biometrias);
    }
}
