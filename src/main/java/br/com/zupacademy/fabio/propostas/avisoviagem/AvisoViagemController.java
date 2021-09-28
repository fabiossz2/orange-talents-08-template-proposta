package br.com.zupacademy.fabio.propostas.avisoviagem;

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

@RestController
class AvisoViagemController {

    private final Logger logger = LoggerFactory.getLogger(AvisoViagemController.class);

    private final CartaoRepository cartaoRepository;
    private final AvisoViagemRepository avisoViagemRepository;
    private final ApiCartaoCredito apiCartaoCredito;

    AvisoViagemController(CartaoRepository cartaoRepository, AvisoViagemRepository avisoViagemRepository,
                          ApiCartaoCredito apiCartaoCredito) {
        this.cartaoRepository = cartaoRepository;
        this.avisoViagemRepository = avisoViagemRepository;
        this.apiCartaoCredito = apiCartaoCredito;
    }

    @PostMapping
    @RequestMapping("/cartoes/{id}/avisos")
    @Transactional
    ResponseEntity<?> avisoViagem(HttpServletRequest HttpServletRequest, @PathVariable(name = "id") String idCartao,
                                  @Valid @RequestBody AvisoViagemRequest request) {
        String ipAddress = HttpServletRequest.getHeader("X-Forwarded-For");
        final String userAgent = HttpServletRequest.getHeader("USER-AGENT");

        if (ipAddress == null) ipAddress = HttpServletRequest.getRemoteAddr();
        final Cartao cartao = this.cartaoRepository.findById(idCartao)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado"));

        try {
            this.apiCartaoCredito.executaAvisoViagem(cartao.getNumero(), request);
            final AvisoViagem avisoViagem = new AvisoViagem(ipAddress, userAgent, cartao);
            this.avisoViagemRepository.save(avisoViagem);
            return ResponseEntity.ok().build();
        } catch (FeignException ex) {
            HttpStatus httpStatus = HttpStatus.resolve(ex.status());

            if (Objects.isNull(httpStatus)) {
                logger.error("Serviço da API de Cartões indisponivél");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Serviço da API de Cartões indisponivél");
            }
            if (Objects.nonNull(httpStatus) && HttpStatus.UNPROCESSABLE_ENTITY.value() == httpStatus.value()) {
                return ResponseEntity.unprocessableEntity().body("Não foi possível adicionar aviso de viagem ao cartão");
            }
            return ResponseEntity.status(httpStatus).body(ex.getMessage());
        }
    }
}
