package br.com.zupacademy.fabio.propostas.bloqueiocartao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface BloqueioCartaoRepository extends CrudRepository<BloqueioCartao, String> {

    @Query("SELECT bloq FROM BloqueioCartao bloq JOIN bloq.cartao card WHERE bloq.status = :status and card.id = :id")
    Optional<BloqueioCartao> findByCartaoStatus(@Param("status") StatusCartao statusCartao, @NotBlank @Param("id") String idCartao);
}
