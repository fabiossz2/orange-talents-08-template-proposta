package br.com.zupacademy.fabio.propostas.associacarteiradigital;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteiraDigitalRepository extends CrudRepository<CarteiraDigital,String> {
}
