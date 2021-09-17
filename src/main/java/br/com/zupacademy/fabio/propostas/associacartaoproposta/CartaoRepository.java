package br.com.zupacademy.fabio.propostas.associacartaoproposta;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface CartaoRepository extends CrudRepository<Cartao, Long> {

    Optional<Cartao> findById(@NotBlank String idCartao);
}

