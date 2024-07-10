package br.com.ifpe.oxefood.modelo.pedido;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Transactional
    public Pedido save(Pedido pedido) {
        pedido.setHabilitado(Boolean.TRUE);
        pedido.setVersao(1L);
        pedido.setDataCriacao(LocalDate.now());
        return repository.save(pedido);
    }

     public List<Pedido> listarTodos() {
  
        return repository.findAll();
    }

    public Pedido obterPorID(Long id) {

        return repository.findById(id).get();
    }

    @Transactional
    public void update(Long id, Pedido pedidoAlterado) {

        Pedido pedido = repository.findById(id).get();
        pedido.setDataPedido(pedidoAlterado.getDataPedido());
        pedido.setCliente(pedidoAlterado.getCliente());
        pedido.setProduto(pedidoAlterado.getProduto());
        pedido.setQuantidadeProdutos(pedidoAlterado.getQuantidadeProdutos());
        pedido.setRua(pedidoAlterado.getRua());
        pedido.setNumero(pedido.getNumero());
        pedido.setBairro(pedido.getBairro());
        pedido.setCidade(pedido.getCidade());
        pedido.setCep(pedido.getCep());
        pedido.setUf(pedidoAlterado.getUf());
        pedido.setComplemento(pedidoAlterado.getComplemento());
        pedido.setVersao(pedido.getVersao() + 1);
        repository.save(pedido);
    }

    @Transactional
    public void delete(Long id) {

        Pedido pedido = repository.findById(id).get();
        pedido.setHabilitado(Boolean.FALSE);
        pedido.setVersao(pedido.getVersao() + 1);

        repository.save(pedido);
    }
}
