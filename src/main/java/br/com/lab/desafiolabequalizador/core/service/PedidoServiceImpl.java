package br.com.lab.desafiolabequalizador.core.service;

import br.com.lab.desafiolabequalizador.core.domain.PedidoLegado;
import br.com.lab.desafiolabequalizador.core.domain.Usuario;
import br.com.lab.desafiolabequalizador.core.ports.driver.PedidoService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Override
    public List<Usuario> converterParaNovoModelo(MultipartFile file, Long idPedido, LocalDate dataInicio,
                                                 LocalDate dataFim) {

        List<PedidoLegado> pedidosLegado = lerArquivoLegado(file);

        return pedidosLegado.stream()
                .filter(pedido -> filtrarPorIdPedido(pedido, idPedido))
                .filter(pedido -> filtrarPorPeriodo(pedido, dataInicio, dataFim))
                .collect(Collectors.groupingBy(PedidoLegado::getIdUsuario))
                .values()
                .stream()
                .map(Usuario::new)
                .collect(Collectors.toList());
    }

    private boolean filtrarPorIdPedido(PedidoLegado pedido, Long idPedido) {
        return Optional.ofNullable(idPedido)
                .map(id -> pedido.getIdPedido().equals(id))
                .orElse(true);
    }

    private boolean filtrarPorPeriodo(PedidoLegado pedido, LocalDate dataInicio, LocalDate dataFim) {
        return Optional.ofNullable(dataInicio)
                .flatMap(start -> Optional.ofNullable(dataFim)
                        .map(end -> validarDatas(start, end, pedido)))
                .orElse(true);
    }

    private static boolean validarDatas(LocalDate dataInicio, LocalDate dataFim, PedidoLegado pedido) {
        LocalDate dataPedido = pedido.getData();
        return (dataPedido.equals(dataInicio) || dataPedido.isAfter(dataInicio)) &&
                (dataPedido.equals(dataFim) || dataPedido.isBefore(dataFim));
    }

    private static List<PedidoLegado> lerArquivoLegado(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return reader.lines()
                    .map(String::trim)
                    .filter(linha -> !linha.isEmpty())
                    .map(PedidoLegado::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter arquivo" + e.getMessage());
        }
    }

}
