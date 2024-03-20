package br.com.ifpe.oxefood.modelo.produto;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

   @Autowired
   private ProdutoRepository repository;

   @Transactional
   public Produto save(Produto produto) {
      produto.setHabilitado(Boolean.TRUE);
      produto.setVersao(1L);
      produto.setDataCriacao(LocalDate.now());
      return repository.save(produto);
   }

   public List<Produto> listarTodos() {
  
        return repository.findAll();
    }

    public Produto obterPorID(Long id) {

        return repository.findById(id).get();
    }

}
