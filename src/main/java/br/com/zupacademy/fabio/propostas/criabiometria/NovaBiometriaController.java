package br.com.zupacademy.fabio.propostas.criabiometria;

import br.com.zupacademy.fabio.propostas.associacartaoproposta.Cartao;
import br.com.zupacademy.fabio.propostas.associacartaoproposta.CartaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.Objects;

@RestController
public class NovaBiometriaController {

    private final CartaoRepository cartaoRepository;
    private final BiometriaRepository biometriaRepository;

    public NovaBiometriaController(CartaoRepository cartaoRepository, BiometriaRepository biometriaRepository) {
        this.cartaoRepository = cartaoRepository;
        this.biometriaRepository = biometriaRepository;
    }

    @PostMapping
    @RequestMapping("/cartoes/{id}/biometrias")
    @Transactional
    public ResponseEntity<?> cria(@RequestBody NovaBiometriaPostRequest request,
                                  @PathVariable(required = true, value = "id") String idCartao, UriComponentsBuilder builder) {

        Cartao cartao = this.cartaoRepository.findById(idCartao)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado"));

        Biometria biometria = request.toModel(cartao);
        cartao.adicionaBiometria(biometria);

        biometriaRepository.save(biometria);

        URI uri = builder.path("/cartoes/{id}/biometria/{id}")
                .buildAndExpand(cartao.getId(), biometria.getId()).toUri();

        return ResponseEntity.created(uri).body(new BiometriaDto(biometria));
    }
}
