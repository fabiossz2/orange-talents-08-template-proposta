package br.com.zupacademy.fabio.propostas.associacarteiradigital;

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
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
class AssociaCarteiraDigitalController {

    private final Logger logger = LoggerFactory.getLogger(AssociaCarteiraDigitalController.class);

    private final CarteiraDigitalRepository carteiraDigitalRepository;
    private final CartaoRepository cartaoRepository;
    private final ApiCartaoCredito apiCartaoCredito;

    AssociaCarteiraDigitalController(CarteiraDigitalRepository carteiraDigitalRepository,
                                     CartaoRepository cartaoRepository, ApiCartaoCredito apiCartaoCredito) {
        this.carteiraDigitalRepository = carteiraDigitalRepository;
        this.cartaoRepository = cartaoRepository;
        this.apiCartaoCredito = apiCartaoCredito;
    }

    @PostMapping
    @Transactional
    @RequestMapping("/cartoes/{id}/carteiras")
    ResponseEntity<?> associa(@PathVariable(name = "id", required = true) String idCartao,
                              @Valid @RequestBody CarteiraDigitalRequest request, UriComponentsBuilder builder) {

        final Cartao cartao = cartaoRepository.findById(idCartao)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado"));

        if (cartao.temCarteiraDigitalAssociada(request.getCarteira()).size() > 0)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Cartão não pode ser associado mais de uma vez a mesma carteira digital");

        try {
            apiCartaoCredito.associaCarteiraDigital(cartao.getNumero(), request);
            final CarteiraDigital carteiraDigital = new CarteiraDigital(request.getEmail(), request.getCarteira(), cartao);
            carteiraDigitalRepository.save(carteiraDigital);
            cartao.adicionaCarteiraDigital(carteiraDigital);
            final URI uri = builder.path("/cartoes/{id}/carteiras/{id}").buildAndExpand(cartao.getId(), carteiraDigital.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (FeignException ex) {
            logger.warn(ex.toString());
            return ResponseEntity.unprocessableEntity().body("Não foi possível associar a carteira digital");
        }
    }
}
