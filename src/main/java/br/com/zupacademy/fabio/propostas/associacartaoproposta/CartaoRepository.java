package br.com.zupacademy.fabio.propostas.associacartaoproposta;

import br.com.zupacademy.fabio.propostas.associacarteiradigital.TipoCarteiraDigital;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface CartaoRepository extends CrudRepository<Cartao, String> {

    Optional<Cartao> findById(@NotBlank String idCartao);

    @Query("SELECT card FROM CarteiraDigital cart JOIN cart.cartao card WHERE card.id = :id " +
            "AND cart.carteiraDigital = :digital")
    Optional<Cartao> findCartaoAndCarteiraDigital(@NotBlank @Param("id") String idCartao,
                                                  @Param("digital") @NotNull TipoCarteiraDigital carteiraDigital);
}

