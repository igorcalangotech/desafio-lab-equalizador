package br.com.lab.desafiolabequalizador.core.ports.driver;

import br.com.lab.desafiolabequalizador.core.domain.Usuario;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface PedidoService {

    List<Usuario> converterParaNovoModelo(MultipartFile file, Long idPedido, LocalDate dataInicio, LocalDate dataFim);

}
