package br.com.lab.desafiolabequalizador.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record PedidoDTO(
        Long idUsuario,
        String nome,
        Long idPedido,
        Long idProduto,
        BigDecimal valor,
        LocalDate data
) {
    public static PedidoDTO fromLinha(String linha) {
        Long idUsuario = Long.parseLong(linha.substring(0, 10).trim());
        String nome = linha.substring(10, 55).trim();
        Long idPedido = Long.parseLong(linha.substring(55, 65).trim());
        Long idProduto = Long.parseLong(linha.substring(65, 75).trim());
        BigDecimal valor = new BigDecimal(linha.substring(75, 87).trim());
        LocalDate data = LocalDate.parse(linha.substring(87, 95).trim(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        return new PedidoDTO(idUsuario, nome, idPedido, idProduto, valor, data);
    }
}
