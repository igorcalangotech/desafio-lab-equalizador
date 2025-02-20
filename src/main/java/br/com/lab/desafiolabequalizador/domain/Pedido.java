package br.com.lab.desafiolabequalizador.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static br.com.lab.desafiolabequalizador.utils.PedidoUtils.singleOrThrow;

@Getter
public class Pedido {

    private final Long id;
    private final BigDecimal valor;
    private final LocalDate data;
    private final List<Produto> produtos;

    public Pedido(List<PedidoLegado> pedidoLegados) {
        this.id = pedidoLegados.stream().map(PedidoLegado::getIdPedido).findFirst().orElse(null);
        this.produtos = pedidoLegados.stream()
                .map(item -> new Produto(item.getIdProduto(), item.getValor()))
                .collect(Collectors.toList());
        this.valor = this.produtos.stream()
                .map(Produto::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.data = extrairData(pedidoLegados);
    }

    private LocalDate extrairData(List<PedidoLegado> itens) {
        List<LocalDate> datas = itens.stream()
                .map(PedidoLegado::getData)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        return singleOrThrow(
                datas,
                () -> new RuntimeException("Há datas diferentes no mesmo pedido: " + datas)
        );
    }
}
