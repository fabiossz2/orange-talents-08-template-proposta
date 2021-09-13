package br.com.zupacademy.fabio.propostas.novaproposta;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NovaPropostaControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();

    @Test
    void deveSalvarUmaPropostaComSucesso() throws Exception {
        NovaPropostaPostRequest propostaPostRequest = new NovaPropostaPostRequest("95148105895",
                "teste@email.com.br", "teste", "Rua José Júlio Burgain 741", new BigDecimal("1000.00"));
        String propostaJson = this.gson.toJson(propostaPostRequest);
        mockMvc.perform(post("/propostas")
                        .contentType("application/json")
                        .content(propostaJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists());
    }

    @ParameterizedTest
    @MethodSource("generationProposta")
    void naoDeveriaSalvarUmaPropostaBadRequest() throws Exception {
        Stream<Arguments> propostas = generationProposta();
        String propostasJson = this.gson.toJson(propostas);
        mockMvc.perform(post("/propostas")
                        .contentType("application/json")
                        .content(propostasJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> generationProposta() {
        return Stream.of(
                Arguments.of(new NovaPropostaPostRequest("111", "", "", "",
                        new BigDecimal("-10.00"))),
                Arguments.of(new NovaPropostaPostRequest("12345678901", "", "", "",
                        null)), Arguments.of(new NovaPropostaPostRequest("1234567891011", "",
                        "", "", null)));
    }
}