package br.com.ifpe.oxefood.modelo.produto;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public class ProdutoException extends RuntimeException {

        public static final String MSG_VALOR_MINIMO_PRODUTO = "Não é permitido que sejam inseridos produtos com um valor menor que R$20 e maior que R$100";

        public ProdutoException(String msg) {
            super(String.format(msg));
        }
    }

    private void validarPrecoProduto(Double valorUnitario) {
        if (valorUnitario < 20 || valorUnitario > 100) {
            throw new ProdutoException(ProdutoException.MSG_VALOR_MINIMO_PRODUTO);
        }
    }

    @Transactional
    public Produto save(Produto produto) {
        validarPrecoProduto(produto.getValorUnitario());
        produto.setHabilitado(Boolean.TRUE);
        produto.setVersao(1L);
        produto.setDataCriacao(LocalDate.now());
        return repository.save(produto);
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Produto obterPorID(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    @Transactional
    public void update(Long id, Produto produtoAlterado) {
        Produto produto = repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        validarPrecoProduto(produtoAlterado.getValorUnitario());
        
        produto.setCategoria(produtoAlterado.getCategoria());
        produto.setTitulo(produtoAlterado.getTitulo());
        produto.setCodigo(produtoAlterado.getCodigo());
        produto.setDescricao(produtoAlterado.getDescricao());
        produto.setValorUnitario(produtoAlterado.getValorUnitario());
        produto.setTempoEntregaMinimo(produtoAlterado.getTempoEntregaMinimo());
        produto.setTempoEntregaMaximo(produtoAlterado.getTempoEntregaMaximo());
        produto.setVersao(produto.getVersao() + 1);
        repository.save(produto);
    }

    @Transactional
    public void delete(Long id) {
        Produto produto = repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setHabilitado(Boolean.FALSE);
        produto.setVersao(produto.getVersao() + 1);

        repository.save(produto);
    }

    public List<Produto> filtrar(String codigo, String titulo, Long idCategoria) {

        List<Produto> listaProdutos = repository.findAll();
 
        if ((codigo != null && !"".equals(codigo)) &&
            (titulo == null || "".equals(titulo)) &&
            (idCategoria == null)) {
                listaProdutos = repository.consultarPorCodigo(codigo);
        } else if (
            (codigo == null || "".equals(codigo)) &&
            (titulo != null && !"".equals(titulo)) &&
            (idCategoria == null)) {    
                listaProdutos = repository.findByTituloContainingIgnoreCaseOrderByTituloAsc(titulo);
        } else if (
            (codigo == null || "".equals(codigo)) &&
            (titulo == null || "".equals(titulo)) &&
            (idCategoria != null)) {
                listaProdutos = repository.consultarPorCategoria(idCategoria); 
        } else if (
            (codigo == null || "".equals(codigo)) &&
            (titulo != null && !"".equals(titulo)) &&
            (idCategoria != null)) {
                listaProdutos = repository.consultarPorTituloECategoria(titulo, idCategoria); 
        }
 
        return listaProdutos;
 }
 
}
