package br.com.zupacademy.fabio.propostas.novaproposta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
public class NovaPropostaController {

    private final PropostaRepository propostaRepository;

    public NovaPropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @PostMapping
    @RequestMapping("/propostas")
    @Transactional
    public ResponseEntity<NovaPropostaDto> cria(@RequestBody @Valid NovaPropostaPostRequest request,
                                                UriComponentsBuilder builder) {
        Proposta proposta = request.toModel();
        this.propostaRepository.save(proposta);
        URI uri = builder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new NovaPropostaDto(proposta));
    }
}
