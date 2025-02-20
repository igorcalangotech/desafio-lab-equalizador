package br.com.lab.desafiolabequalizador.backend;

import br.com.lab.desafiolabequalizador.api.v1.PedidoRestService;
import br.com.lab.desafiolabequalizador.core.ports.driver.PedidoService;
import br.com.lab.desafiolabequalizador.domain.PedidoLegado;
import br.com.lab.desafiolabequalizador.domain.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PedidoController implements PedidoRestService {

    private final PedidoService service;

    @Override
    public ResponseEntity<List<Usuario>> converterPedido(MultipartFile file) {
        return ResponseEntity.ok(service.converterPedido(file));
    }

}
