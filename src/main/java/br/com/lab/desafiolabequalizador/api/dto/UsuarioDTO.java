package br.com.lab.desafiolabequalizador.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UsuarioDTO(

        @JsonProperty("user_id")
        Long id,

        @JsonProperty("name")
        String nome,

        @JsonProperty("orders")
        List<PedidoDTO> pedidos
) {
}
