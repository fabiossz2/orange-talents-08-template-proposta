package br.com.zupacademy.fabio.propostas.associacartaoproposta;

import br.com.zupacademy.fabio.propostas.externo.ApiCartaoCredito;
import br.com.zupacademy.fabio.propostas.novaproposta.Proposta;
import br.com.zupacademy.fabio.propostas.novaproposta.PropostaRepository;
import br.com.zupacademy.fabio.propostas.novaproposta.PropostaStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class ObtemNumeroCartaoTask {

    private final PropostaRepository propostaRepository;
    private final ApiCartaoCredito apiCartaoCredito;
    private final CartaoRepository cartaoRepository;

    private final Logger logger = LoggerFactory.getLogger(ObtemNumeroCartaoTask.class);

    public ObtemNumeroCartaoTask(PropostaRepository propostaRepository, ApiCartaoCredito apiCartaoCredito,
                                 CartaoRepository cartaoRepository) {
        this.propostaRepository = propostaRepository;
        this.apiCartaoCredito = apiCartaoCredito;
        this.cartaoRepository = cartaoRepository;
    }

    @Scheduled(fixedDelayString = "${periodicidade.executa.operacao.obtem.numero.cartao.credito}")
    @Transactional
    public synchronized void execute() {
        String idCartao = null;
        Map<String, String> mapDadosSolicitante;

        Collection<Proposta> propostasElegiveis = propostaRepository.findPropostaByStatus(PropostaStatus.ELEGIVEL);

        for (Proposta p : propostasElegiveis) {
            mapDadosSolicitante = new HashMap<>();
            mapDadosSolicitante.put("documento", p.getDocumento());
            mapDadosSolicitante.put("nome", p.getNome());
            mapDadosSolicitante.put("idProposta", p.getId());
            try {
                String jsonDadosCartao = this.apiCartaoCredito.obtemNumeroCartaoCredito(mapDadosSolicitante);
                if (Objects.nonNull(jsonDadosCartao)) {
                    idCartao = new ObjectMapper().readTree(jsonDadosCartao).get("id").asText();
                }
                if (Objects.nonNull(idCartao)) {
                    Cartao cartao = new Cartao(idCartao, p);
                    this.cartaoRepository.save(cartao);
                }
            } catch (FeignException | JsonProcessingException ex) {
                logger.warn(ex.toString());
            }
        }
    }
}
