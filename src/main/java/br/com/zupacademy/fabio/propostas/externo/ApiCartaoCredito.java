package br.com.zupacademy.fabio.propostas.externo;

import br.com.zupacademy.fabio.propostas.associacarteiradigital.CarteiraDigitalRequest;
import br.com.zupacademy.fabio.propostas.avisoviagem.AvisoViagemRequest;
import br.com.zupacademy.fabio.propostas.bloqueiocartao.BloqueioCartaoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "cartaoCredito", url = "${feign.client.url.api.cartoes}")
public interface ApiCartaoCredito {

    @PostMapping
    String obtemNumeroCartaoCredito(@RequestBody Map<String, String> dadosSolicitante);

    @PostMapping("/{id}/bloqueios")
    void executaBloqueioCartao(@PathVariable(name = "id") String numeroCartao, @RequestBody BloqueioCartaoRequest request);

    @PostMapping("/{id}/avisos")
    void executaAvisoViagem(@PathVariable(name = "id") String numeroCartao, @RequestBody AvisoViagemRequest request);
}
