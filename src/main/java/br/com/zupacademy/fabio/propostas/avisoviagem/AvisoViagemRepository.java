package br.com.zupacademy.fabio.propostas.avisoviagem;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvisoViagemRepository extends CrudRepository<AvisoViagem, String> {
}
