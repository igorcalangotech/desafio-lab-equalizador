package br.com.lab.desafiolabequalizador.api.dto;

import java.util.List;

public record UsuarioDTO(
        Long id,
        String nome,
        List<PedidoDTO> pedidos
) {
}
