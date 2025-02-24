package br.com.lab.desafiolabequalizador.backend;

import br.com.lab.desafiolabequalizador.api.dto.UsuarioDTO;
import br.com.lab.desafiolabequalizador.api.v1.PedidoRestService;
import br.com.lab.desafiolabequalizador.core.ports.driver.PedidoService;
import br.com.lab.desafiolabequalizador.mapper.UsuarioMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PedidoController implements PedidoRestService {

    private final PedidoService service;
    private final UsuarioMapper mapper;

    @Override
    public ResponseEntity<List<UsuarioDTO>> converterPedido(
            @NotNull MultipartFile file,
            Long idPedido,
            LocalDate dataInicio,
            LocalDate dataFim
    ) {
        return ResponseEntity.ok(
                mapper.toDto(service.converterParaNovoModelo(file, idPedido, dataInicio, dataFim))
        );
    }

}
