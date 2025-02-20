package br.com.lab.desafiolabequalizador.domain;

import br.com.lab.desafiolabequalizador.utils.PedidoUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static br.com.lab.desafiolabequalizador.utils.PedidoUtils.singleOrThrow;

@Getter
@AllArgsConstructor
public class Usuario {

    private Long id;
    private String nome;
    private List<Pedido> pedidos;

    public Usuario(List<PedidoLegado> pedidosLegado) {
        Map<Long, List<PedidoLegado>> collect = pedidosLegado.stream()
                .collect(Collectors.groupingBy(PedidoLegado::getIdPedido));
        this.id = pedidosLegado.stream().map(PedidoLegado::getIdUsuario).findFirst().orElse(null);
        this.nome = extrairNome(pedidosLegado);
        this.pedidos = pedidosLegado.stream()
                .collect(Collectors.groupingBy(PedidoLegado::getIdPedido))
                .entrySet().stream()
                .map(this::converterPedido)
                .collect(Collectors.toList());
    }

    private Pedido converterPedido(Map.Entry<Long, List<PedidoLegado>> pedidoEntry) {
        return new Pedido(pedidoEntry.getValue());
    }

    private String extrairNome(List<PedidoLegado> pedidos) {
        List<String> nomes = pedidos.stream()
                .map(PedidoLegado::getNome)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        return singleOrThrow(
                nomes,
                () -> new RuntimeException("Há nomes diferentes nos pedidos para o mesmo usuário: " + nomes)
        );
    }
}
