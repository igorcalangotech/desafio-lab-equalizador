package br.com.lab.desafiolabequalizador.core.ports.driver;

import br.com.lab.desafiolabequalizador.domain.Usuario;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PedidoService {

    List<Usuario> converterPedido(MultipartFile file);

}
