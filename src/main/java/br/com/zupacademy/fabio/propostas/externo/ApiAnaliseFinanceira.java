package br.com.zupacademy.fabio.propostas.externo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "analiseFinanceira", url = "${feign.client.url.api.analise.financeira}")
public interface ApiAnaliseFinanceira {

    @PostMapping
    String analiseDadosFinanceiros(@RequestBody Map<String, String> dadosSolicitante);
}
