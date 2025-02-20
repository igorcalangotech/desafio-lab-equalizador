package br.com.lab.desafiolabequalizador.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoDTO(
        Long id,
        BigDecimal valor,
        LocalDate data,
        List<ProdutoDTO> produtos
) {
}
