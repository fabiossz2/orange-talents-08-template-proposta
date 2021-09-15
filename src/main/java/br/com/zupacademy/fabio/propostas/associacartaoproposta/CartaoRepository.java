package br.com.zupacademy.fabio.propostas.associacartaoproposta;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoRepository extends CrudRepository<Cartao, Long> {
}
