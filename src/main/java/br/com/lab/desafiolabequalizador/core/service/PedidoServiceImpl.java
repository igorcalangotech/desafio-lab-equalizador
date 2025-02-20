package br.com.lab.desafiolabequalizador.core.service;

import br.com.lab.desafiolabequalizador.core.ports.driver.PedidoService;
import br.com.lab.desafiolabequalizador.domain.Pedido;
import br.com.lab.desafiolabequalizador.domain.PedidoLegado;
import br.com.lab.desafiolabequalizador.domain.Produto;
import br.com.lab.desafiolabequalizador.domain.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Override
    public List<Usuario> converterPedido(MultipartFile file) {
        List<PedidoLegado> pedidosLegado = lerArquivoLegado(file);
        Map<Long, List<PedidoLegado>> pedidosPorUsuario = agruparPedidosPorUsuario(pedidosLegado);
        return pedidosPorUsuario.entrySet().stream()
                .map(this::converterUsuario)
                .collect(Collectors.toList());
    }

    private Map<Long, List<PedidoLegado>> agruparPedidosPorUsuario(List<PedidoLegado> pedidosLegado) {
        return pedidosLegado.stream()
                .collect(Collectors.groupingBy(PedidoLegado::getIdUsuario));
    }

    private Usuario converterUsuario(Map.Entry<Long, List<PedidoLegado>> entry) {
        Long userId = entry.getKey();
        List<PedidoLegado> pedidosDoUsuario = entry.getValue();
        String nome = extrairNome(pedidosDoUsuario);
        List<Pedido> pedidos = converterPedidos(pedidosDoUsuario);
        return new Usuario(userId, nome, pedidos);
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

    private List<Pedido> converterPedidos(List<PedidoLegado> pedidosDoUsuario) {
        Map<Long, List<PedidoLegado>> pedidosAgrupados = pedidosDoUsuario.stream()
                .collect(Collectors.groupingBy(PedidoLegado::getIdPedido));
        return pedidosAgrupados.entrySet().stream()
                .map(this::converterPedido)
                .collect(Collectors.toList());
    }

    private Pedido converterPedido(Map.Entry<Long, List<PedidoLegado>> pedidoEntry) {

        Long idPedido = pedidoEntry.getKey();
        List<PedidoLegado> itens = pedidoEntry.getValue();
        LocalDate dataPedido = extrairData(itens);
        List<Produto> produtos = itens.stream()
                .map(item -> new Produto(item.getIdProduto(), item.getValor()))
                .collect(Collectors.toList());
        BigDecimal total = produtos.stream()
                .map(Produto::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        return new Pedido(idPedido, total, dataPedido, produtos);
        return null;

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

    private static List<PedidoLegado> lerArquivoLegado(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return reader.lines().map(String::trim)
                    .filter(linha -> !linha.isEmpty())
                    .map(PedidoLegado::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter arquivo" + e.getMessage());
        }
    }

    private <T> T singleOrThrow(List<T> list, Supplier<? extends RuntimeException> exceptionSupplier) {
        return list.size() > 1
                ? throwException(exceptionSupplier)
                : list.stream().findFirst().orElse(null);
    }

    private static <T> T throwException(Supplier<? extends RuntimeException> exceptionSupplier) {
        throw exceptionSupplier.get();
    }

}
