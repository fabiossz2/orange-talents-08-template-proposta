package br.com.zupacademy.fabio.propostas.novaproposta;

import br.com.zupacademy.fabio.propostas.commons.EncryptorsUtil;
import br.com.zupacademy.fabio.propostas.externo.ApiAnaliseFinanceira;
import br.com.zupacademy.fabio.propostas.metrics.PropostasMetrics;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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
    private Tracer tracer;

    public NovaPropostaController(PropostaRepository propostaRepository, ApiAnaliseFinanceira apiAnaliseFinanceira,
                                  PropostasMetrics propostasMetrics, Tracer tracer) {
        this.propostaRepository = propostaRepository;
        this.apiAnaliseFinanceira = apiAnaliseFinanceira;
        this.propostasMetrics = propostasMetrics;
        this.tracer = tracer;
    }

    @PostMapping
    @RequestMapping("/propostas")
    @Transactional
    public ResponseEntity<?> cria(@RequestBody @Valid NovaPropostaPostRequest request,
                                  UriComponentsBuilder builder) throws FeignException {
        Span span = tracer.activeSpan();
        span.setTag("usuario.email", request.getEmail());
        span.setBaggageItem("user.email", request.getEmail());

        Optional<Proposta> propostaOptional = propostaRepository.findByDocumento(EncryptorsUtil.encripta(request.getDocumento()));
        if (propostaOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("solicitante já requisitou uma proposta");
        }

        Proposta proposta = request.toModel();
        this.propostaRepository.save(proposta);

        Map<String, String> mapaDadosSolicitante = proposta.criaSolicitacaoAnalise();

        try {
            apiAnaliseFinanceira.analiseDadosFinanceiros(mapaDadosSolicitante);
            proposta.setStatus(PropostaStatus.ELEGIVEL);
            logger.info(String.format("Proposta - solicitante: %s endereco-solicitante: %s salario: %.2f - ELEGIVEL",
                    proposta.getId(), proposta.getEndereco(), proposta.getSalario()));
        } catch (FeignException ex) {
            HttpStatus httpStatus = HttpStatus.resolve(ex.status());

            if (Objects.isNull(httpStatus)) {
                logger.error("Serviço da API de análise financeira indisponivél");
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Serviço da API de análise financeira indisponivél");
            }

            if (Objects.nonNull(httpStatus) && HttpStatus.UNPROCESSABLE_ENTITY.value() == httpStatus.value()) {
                proposta.setStatus(PropostaStatus.NAO_ELEGIVEL);
                logger.info(String.format("Proposta - solicitante: %s endereco-solicitante: %s salario: %.2f - NAO ELEGIVEL",
                        proposta.getId(), proposta.getEndereco(), proposta.getSalario()));
            }
        }
        this.propostasMetrics.counterPropostasCriadas();
        URI uri = builder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new NovaPropostaDto(proposta));
    }
}
