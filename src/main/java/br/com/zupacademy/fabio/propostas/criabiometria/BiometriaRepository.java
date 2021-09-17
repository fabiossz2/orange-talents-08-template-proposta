package br.com.zupacademy.fabio.propostas.criabiometria;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiometriaRepository extends CrudRepository<Biometria, Long> {
}
