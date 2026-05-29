package br.com.missaoespacial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NasaApodResponse(
        String title,
        String explanation,
        String url,
        String hdurl,
        @JsonProperty("media_type") String mediaType,
        String date,
        String copyright
) {
}
