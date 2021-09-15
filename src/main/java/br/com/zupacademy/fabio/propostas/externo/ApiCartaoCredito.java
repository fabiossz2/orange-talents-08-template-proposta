package br.com.zupacademy.fabio.propostas.externo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "cartaoCredito", url = "${feign.client.url.api.cartoes}")
public interface ApiCartaoCredito {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    String obtemNumeroCartaoCredito(@RequestBody Map<String, String> dadosSolicitante);
}
