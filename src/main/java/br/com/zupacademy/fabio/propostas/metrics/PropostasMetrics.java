package br.com.zupacademy.fabio.propostas.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class PropostasMetrics {

    private final MeterRegistry meterRegistry;

    private Collection<Tag> tags = List.of(Tag.of("emissora", "Mastercard"), Tag.of("banco", "Itaú"));

    public PropostasMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void counterPropostasCriadas() {
        final Counter contadorDePropostasCriadas =
                this.meterRegistry.counter("proposta_criada", this.tags);
        contadorDePropostasCriadas.increment();
    }
}
