package br.com.ifpe.oxefood.api.pedido;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.ifpe.oxefood.modelo.pedido.Pedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataPedido;

    private String cliente;

    private String produto;

    private int quantidadeProdutos;

    private String rua;

    private String numero;

    private String bairro;

    private String cidade;

    private String cep;

    private String uf;

    private String complemento;

    public Pedido build() {

        return Pedido.builder()
                .dataPedido(dataPedido)
                .cliente(cliente)
                .produto(produto)
                .quantidadeProdutos(quantidadeProdutos)
                .rua(rua)
                .numero(numero)
                .bairro(bairro)
                .cidade(cidade)
                .cep(cep)
                .uf(uf)
                .complemento(complemento)
                .build();

    }

}
