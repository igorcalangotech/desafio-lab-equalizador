package br.com.lab.desafiolabequalizador.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ProdutoDTO(

        @JsonProperty("product_id")
        Long id,

        @JsonProperty("value")
        BigDecimal valor
) {
}
