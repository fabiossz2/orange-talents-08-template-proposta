package br.com.zupacademy.fabio.propostas.acompanhamentoproposta;

import br.com.zupacademy.fabio.propostas.novaproposta.Proposta;
import br.com.zupacademy.fabio.propostas.novaproposta.PropostaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AcompanhaPropostaController {

    private final PropostaRepository propostaRepository;

    public AcompanhaPropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @GetMapping
    @RequestMapping("/propostas/{id}")
    public ResponseEntity<?> getById(@PathVariable(required = true) String id) {
        Optional<Proposta> optionalProposta = this.propostaRepository.findById(id);
        if (optionalProposta.isPresent()) {
            return ResponseEntity.ok(new PropostaDto(optionalProposta.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
