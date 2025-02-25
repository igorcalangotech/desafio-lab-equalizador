package br.com.lab.desafiolabequalizador.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoDTO(

        @JsonProperty("order_id")
        Long id,

        @JsonProperty("total")
        BigDecimal valor,

        @JsonProperty("date")
        LocalDate data,

        @JsonProperty("products")
        List<ProdutoDTO> produtos
) {
}
