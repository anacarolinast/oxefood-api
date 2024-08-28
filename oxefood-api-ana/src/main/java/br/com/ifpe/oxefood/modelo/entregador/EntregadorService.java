package br.com.ifpe.oxefood.modelo.entregador;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

@Service
public class EntregadorService {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public class EntidadeNaoEncontradaException extends RuntimeException {

        public EntidadeNaoEncontradaException(String entidade, Long id) {
            super(String.format("Não foi encontrado(a) um(a) %s com o id %s", entidade, id.toString()));
        }
    }

    @Autowired
    private EntregadorRepository repository;

    @Transactional
    public Entregador save(Entregador entregador) {
        entregador.setHabilitado(Boolean.TRUE);
        entregador.setVersao(1L);
        entregador.setDataCriacao(LocalDate.now());
        return repository.save(entregador);
    }

     public List<Entregador> listarTodos() {
  
        return repository.findAll();
    }

    public Entregador obterPorID(Long id) {
        Optional<Entregador> consulta = repository.findById(id);
  
       if (consulta.isPresent()) {
           return consulta.get();
       } else {
           throw new EntidadeNaoEncontradaException("Cliente", id);
       }

    }

    @Transactional
    public void update(Long id, Entregador entregadorAlterado) {

        Entregador entregador = repository.findById(id).get();
        entregador.setNome(entregadorAlterado.getNome());
        entregador.setCpf(entregadorAlterado.getCpf());
        entregador.setRg(entregadorAlterado.getRg());
        entregador.setDataNascimento(entregadorAlterado.getDataNascimento());
        entregador.setFoneCelular(entregadorAlterado.getFoneCelular());
        entregador.setFoneFixo(entregadorAlterado.getFoneFixo());
        entregador.setQtdEntregas(entregadorAlterado.getQtdEntregas());
        entregador.setValorFrete(entregadorAlterado.getValorFrete());
        entregador.setRua(entregadorAlterado.getRua());
        entregador.setNumero(entregador.getNumero());
        entregador.setBairro(entregador.getBairro());
        entregador.setCidade(entregador.getCidade());
        entregador.setCep(entregador.getCep());
        entregador.setUf(entregadorAlterado.getUf());
        entregador.setComplemento(entregadorAlterado.getComplemento());
        entregador.setAtivo(entregadorAlterado.getAtivo());
        entregador.setVersao(entregador.getVersao() + 1);
        repository.save(entregador);
    }

    @Transactional
    public void delete(Long id) {

        Entregador entregador = repository.findById(id).get();
        entregador.setHabilitado(Boolean.FALSE);
        entregador.setVersao(entregador.getVersao() + 1);

        repository.save(entregador);
    }
}
