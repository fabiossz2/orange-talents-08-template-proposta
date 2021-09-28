package br.com.zupacademy.fabio.propostas.bloqueiocartao;

import br.com.zupacademy.fabio.propostas.associacartaoproposta.Cartao;
import br.com.zupacademy.fabio.propostas.associacartaoproposta.CartaoRepository;
import br.com.zupacademy.fabio.propostas.externo.ApiCartaoCredito;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@RestController
public class BloqueioCartaoController {

    private final Logger logger = LoggerFactory.getLogger(BloqueioCartaoController.class);

    private final CartaoRepository cartaoRepository;
    private final BloqueioCartaoRepository bloqueioCartaoRepository;
    private final ApiCartaoCredito apiCartaoCredito;

    public BloqueioCartaoController(CartaoRepository cartaoRepository, BloqueioCartaoRepository bloqueioCartaoRepository,
                                    ApiCartaoCredito apiCartaoCredito) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueioCartaoRepository = bloqueioCartaoRepository;
        this.apiCartaoCredito = apiCartaoCredito;
    }

    @PostMapping
    @RequestMapping("/cartoes/{id}/bloqueios")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    void bloqueia(HttpServletRequest HttpServletRequest, @PathVariable(name = "id") String idCartao,
                  @Valid @RequestBody BloqueioCartaoRequest request) {
        String ipAddress = HttpServletRequest.getHeader("X-Forwarded-For");
        final String userAgent = HttpServletRequest.getHeader("USER-AGENT");

        if (ipAddress == null) ipAddress = HttpServletRequest.getRemoteAddr();

        final Cartao cartao = this.cartaoRepository
                .findById(idCartao)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado"));

        final Optional<BloqueioCartao> optionalBloqueioCartao = this.bloqueioCartaoRepository.findByCartaoStatus(StatusCartao.BLOQUEADO, cartao.getId());
        if (optionalBloqueioCartao.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartão já encontra-se bloqueado");
        }
        try {
            this.apiCartaoCredito.executaBloqueioCartao(cartao.getNumero(), request);
            final BloqueioCartao bloqueioCartao = new BloqueioCartao(ipAddress, userAgent, StatusCartao.BLOQUEADO, cartao);
            this.bloqueioCartaoRepository.save(bloqueioCartao);
        } catch (FeignException ex) {
            HttpStatus httpStatus = HttpStatus.resolve(ex.status());

            if (Objects.isNull(httpStatus)) {
                logger.error("Serviço da API de Cartões indisponivél");
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Serviço da API de Cartões indisponivél");
            }
            if (Objects.nonNull(httpStatus) && HttpStatus.UNPROCESSABLE_ENTITY.value() == httpStatus.value()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Não foi possível adicionar aviso de viagem ao cartão");
            }
            throw new ResponseStatusException(httpStatus, ex.getMessage());
        }
    }
}
