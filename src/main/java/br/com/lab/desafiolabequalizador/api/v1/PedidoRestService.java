package br.com.lab.desafiolabequalizador.api.v1;


import br.com.lab.desafiolabequalizador.api.dto.UsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Pedidos", description = "Endpoints para tratar dos pedidos do novo sistema")
@RequestMapping(path = PedidoRestService.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public interface PedidoRestService {

    String PATH = "/api/v1/pedido";
    String CONVERTER_ARQUIVO_LEGADO = "/legado/converter";

    @Operation(summary = "Converte os pedidos", description = "Realiza a conversão dos pedidos do sistema legado para o novo sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos convertidos com sucesso")
    })
    @PostMapping(CONVERTER_ARQUIVO_LEGADO)
    ResponseEntity<List<UsuarioDTO>> converterPedido(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "orderId", required = false) Long idPedido,
            @RequestParam(value = "beginDate", required = false) LocalDate dataInicio,
            @RequestParam(value = "endDate", required = false) LocalDate dataFim
    );
}
