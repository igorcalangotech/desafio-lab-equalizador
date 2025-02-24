package br.com.lab.desafiolabequalizador.core.service;

import br.com.lab.desafiolabequalizador.core.ports.driver.PedidoService;
import br.com.lab.desafiolabequalizador.domain.PedidoLegado;
import br.com.lab.desafiolabequalizador.domain.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Override
    public List<Usuario> converterParaNovoModelo(MultipartFile file) {
        List<PedidoLegado> pedidosLegado = lerArquivoLegado(file);
        Map<Long, List<PedidoLegado>> pedidosPorUsuario = pedidosLegado.stream()
                .collect(Collectors.groupingBy(PedidoLegado::getIdUsuario));

        return pedidosPorUsuario.values().stream()
                .map(Usuario::new)
                .collect(Collectors.toList());
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
