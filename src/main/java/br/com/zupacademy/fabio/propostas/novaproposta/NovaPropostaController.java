package br.com.zupacademy.fabio.propostas.novaproposta;

import br.com.zupacademy.fabio.propostas.externo.ApiAnaliseFinanceira;
import br.com.zupacademy.fabio.propostas.metrics.PropostasMetrics;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(NovaPropostaController.class);

    private PropostaRepository propostaRepository;
    private ApiAnaliseFinanceira apiAnaliseFinanceira;
    private PropostasMetrics propostasMetrics;

    public NovaPropostaController(PropostaRepository propostaRepository, ApiAnaliseFinanceira apiAnaliseFinanceira,
                                  PropostasMetrics propostasMetrics) {
        this.propostaRepository = propostaRepository;
        this.apiAnaliseFinanceira = apiAnaliseFinanceira;
        this.propostasMetrics = propostasMetrics;
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
            logger.info(String.format("Proposta - solicitante: %s endereco-solicitante: %s salario: %.2f criada com sucesso",
                    proposta.getId(), proposta.getEndereco(), proposta.getSalario()));
        } catch (FeignException ex) {
            logger.warn(ex.toString());
            HttpStatus httpStatus = HttpStatus.resolve(ex.status());

            if (Objects.isNull(httpStatus)) httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

            if (HttpStatus.UNPROCESSABLE_ENTITY.value() == httpStatus.value())
                proposta.setStatus(PropostaStatus.NAO_ELEGIVEL);
        }
        this.propostasMetrics.counterPropostasCriadas();
        URI uri = builder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new NovaPropostaDto(proposta));
    }
}
