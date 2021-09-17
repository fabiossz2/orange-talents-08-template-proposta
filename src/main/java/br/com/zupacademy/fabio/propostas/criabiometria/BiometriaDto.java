package br.com.zupacademy.fabio.propostas.criabiometria;

public class BiometriaDto {

    private String fingerprint;

    public BiometriaDto(Biometria biometria) {
        this.fingerprint = biometria.getFingerprint();
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
