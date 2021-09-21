package br.com.zupacademy.fabio.propostas.bloqueiocartao;

import br.com.zupacademy.fabio.propostas.associacartaoproposta.Cartao;
import br.com.zupacademy.fabio.propostas.associacartaoproposta.CartaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@RestController
public class BloqueioCartaoController {

    private final CartaoRepository cartaoRepository;
    private final BloqueioCartaoRepository bloqueioCartaoRepository;

    public BloqueioCartaoController(CartaoRepository cartaoRepository, BloqueioCartaoRepository bloqueioCartaoRepository) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueioCartaoRepository = bloqueioCartaoRepository;
    }

    @PostMapping
    @RequestMapping("/cartoes/{id}/bloqueios")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    void bloqueia(HttpServletRequest request, @PathVariable(name = "id", required = true) String idCartao) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        final String userAgent = request.getHeader("USER-AGENT");

        if (ipAddress == null) ipAddress = request.getRemoteAddr();

        final Cartao cartao = this.cartaoRepository
                .findById(idCartao)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado"));

        final Optional<BloqueioCartao> optionalBloqueioCartao = this.bloqueioCartaoRepository.findByCartaoStatus(StatusCartao.BLOQUEADO, cartao.getId());
        if (optionalBloqueioCartao.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartão já encontra-se bloqueado");
        }
        final BloqueioCartao bloqueioCartao = new BloqueioCartao(ipAddress, userAgent, StatusCartao.BLOQUEADO, cartao);
        this.bloqueioCartaoRepository.save(bloqueioCartao);
    }
}
