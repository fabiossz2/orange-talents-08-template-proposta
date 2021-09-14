package br.com.zupacademy.fabio.propostas.novaproposta;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropostaRepository extends CrudRepository<Proposta, Long> {

    Optional<Proposta> findByDocumento(String documento);
}
