package br.com.missaoespacial.service;

import br.com.missaoespacial.dto.NasaApodResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class NasaService {

    private final RestClient restClient;
    private final String apiKey;

    public NasaService(@Value("${nasa.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restClient = RestClient.create("https://api.nasa.gov");
    }

    public NasaApodResponse buscarFotoDoDia() {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/planetary/apod")
                            .queryParam("api_key", apiKey)
                            .build())
                    .retrieve()
                    .body(NasaApodResponse.class);
        } catch (RestClientException exception) {
            return new NasaApodResponse(
                    "NASA APOD indisponivel",
                    "Nao foi possivel carregar a foto astronomica da NASA agora. Verifique a conexao ou configure uma NASA_API_KEY valida.",
                    "/img/foguete.png",
                    "/img/foguete.png",
                    "image",
                    "",
                    "Central de Missoes Espaciais"
            );
        }
    }
}
