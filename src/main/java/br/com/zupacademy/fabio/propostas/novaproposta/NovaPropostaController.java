package br.com.zupacademy.fabio.propostas.novaproposta;

import br.com.zupacademy.fabio.propostas.externo.ApiAnaliseFinanceira;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class NovaPropostaController {

    private PropostaRepository propostaRepository;
    private ApiAnaliseFinanceira apiAnaliseFinanceira;

    public NovaPropostaController(PropostaRepository propostaRepository, ApiAnaliseFinanceira apiAnaliseFinanceira) {
        this.propostaRepository = propostaRepository;
        this.apiAnaliseFinanceira = apiAnaliseFinanceira;
    }

    @PostMapping
    @RequestMapping("/propostas")
    @Transactional
    public ResponseEntity<?> cria(@RequestBody @Valid NovaPropostaPostRequest request,
                                  UriComponentsBuilder builder) throws FeignException {
        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(request.getDocumento());
        if (propostaOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("solicitante já requisitou uma proposta");
        }

        Proposta proposta = request.toModel();
        this.propostaRepository.save(proposta);

        Map<String, String> mapaDadosSolicitante = proposta.criaSolicitacaoAnalise();

        try {
            apiAnaliseFinanceira.analiseDadosFinanceiros(mapaDadosSolicitante);
            proposta.setStatus(PropostaStatus.ELEGIVEL);
        } catch (FeignException ex) {
            HttpStatus httpStatus = HttpStatus.resolve(ex.status());

            if (Objects.isNull(httpStatus)) httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

            if (HttpStatus.UNPROCESSABLE_ENTITY.value() == httpStatus.value())
                proposta.setStatus(PropostaStatus.NAO_ELEGIVEL);
        }

        URI uri = builder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new NovaPropostaDto(proposta));
    }
}
