package br.com.zupacademy.fabio.propostas.associacarteiradigital;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CarteiraDigitalRequest {

    @NotBlank
    @Email
    private String email;

    @NotNull
    private TipoCarteiraDigital carteira;

    public CarteiraDigitalRequest(@NotBlank @Email String email, @NotNull TipoCarteiraDigital carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public TipoCarteiraDigital getCarteira() {
        return carteira;
    }
}
