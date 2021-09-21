package br.com.zupacademy.fabio.propostas.novaproposta;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface PropostaRepository extends CrudRepository<Proposta, String> {

    Optional<Proposta> findByDocumento(String documento);

    @Query("SELECT prop FROM Cartao card RIGHT JOIN card.proposta prop WHERE prop.status = :status AND card IS NULL")
    Collection<Proposta> findPropostaByStatus(@Param("status") PropostaStatus status);

    Optional<Proposta> findById(@NotBlank String id);
}
