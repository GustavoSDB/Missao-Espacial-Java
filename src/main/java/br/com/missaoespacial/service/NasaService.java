package br.com.missaoespacial.service;

import br.com.missaoespacial.dto.NasaApodResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NasaService {

    private final RestClient restClient;
    private final String apiKey;

    public NasaService(@Value("${nasa.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restClient = RestClient.create("https://api.nasa.gov");
    }

    public NasaApodResponse buscarFotoDoDia() {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/planetary/apod")
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .body(NasaApodResponse.class);
    }
}
