package br.com.lab.desafiolabequalizador.service;

import br.com.lab.desafiolabequalizador.controller.dto.PedidoDTO;
import br.com.lab.desafiolabequalizador.domain.Pedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ConversorPedidoTest {

    @Test
    public void deveConverterUmaLinhaEmPedidoDTO() {

        String linha = "0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308";
        Pedido pedido = new Pedido(linha);

        Assertions.assertNotNull(pedido);
        assertAll(()->{
            assertEquals(70, pedido.getIdUsuario());
            assertEquals("Palmer Prosacco", pedido.getNome());
            assertEquals(753, pedido.getIdPedido());
            assertEquals(3, pedido.getIdProduto());
            assertEquals(BigDecimal.valueOf(1836.74), pedido.getValor());
            assertEquals(LocalDate.of(2021, Month.MARCH, 8), pedido.getData());
        });
    }

}
