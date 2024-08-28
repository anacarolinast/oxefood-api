package br.com.ifpe.oxefood.modelo.produto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
   @Query(value = "SELECT p FROM Produto p WHERE p.codigo = :codigo")
   List<Produto> consultarPorCodigo(String codigo);

   List<Produto> findByTituloContainingIgnoreCaseOrderByTituloAsc(String titulo);

   @Query(value = "SELECT p FROM Produto p WHERE p.categoria.id = :idCategoria")
   List<Produto> consultarPorCategoria(Long idCategoria);

   @Query(value = "SELECT p FROM Produto p WHERE p.titulo like %:titulo% AND p.categoria.id = :idCategoria")
   List<Produto> consultarPorTituloECategoria(String titulo, Long idCategoria);

}
