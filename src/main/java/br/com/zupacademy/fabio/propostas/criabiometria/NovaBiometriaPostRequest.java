package br.com.zupacademy.fabio.propostas.criabiometria;

import br.com.zupacademy.fabio.propostas.associacartaoproposta.Cartao;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import java.util.Base64;


public class NovaBiometriaPostRequest {

    @NotBlank
    private String fingerprint;

    public Biometria toModel(Cartao cartao) {
        this.biometriaIsValid();
        return new Biometria(this.fingerprint, cartao);
    }
    public void biometriaIsValid(){
        try{
            byte[] fingerprintByte = Base64.getDecoder().decode(this.fingerprint);
            String fingerprintConvert = Base64.getEncoder().encodeToString(fingerprintByte);
            Assert.isTrue(fingerprintConvert.equals(this.fingerprint), "Base64 Inválida");
        }catch(IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fingerprint inválida");
        }
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
