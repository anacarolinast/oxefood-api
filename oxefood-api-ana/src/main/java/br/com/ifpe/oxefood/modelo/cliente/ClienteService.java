package br.com.ifpe.oxefood.modelo.cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.ifpe.oxefood.modelo.acesso.UsuarioService;
import br.com.ifpe.oxefood.modelo.endereco.EnderecoCliente;
import br.com.ifpe.oxefood.modelo.endereco.EnderecoClienteRepository;
import br.com.ifpe.oxefood.modelo.mensagens.EmailService;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EnderecoClienteRepository enderecoClienteRepository;

    @Autowired
    private EmailService emailService;

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public class EntidadeNaoEncontradaException extends RuntimeException {

        public EntidadeNaoEncontradaException(String entidade, Long id) {
            super(String.format("Não foi encontrado(a) um(a) %s com o id %s", entidade, id.toString()));
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public class TelefoneInvalidoException extends RuntimeException {

        public TelefoneInvalidoException(String mensagem) {
            super(mensagem);
        }
    }

    private boolean telefoneComPrefixo81(String telefone) {
        return telefone != null && telefone.startsWith("81");
    }

    @Transactional
    public Cliente save(Cliente cliente) {
        if (!telefoneComPrefixo81(cliente.getFoneCelular())
                || (cliente.getFoneFixo() != null && !telefoneComPrefixo81(cliente.getFoneFixo()))) {
            throw new TelefoneInvalidoException("O telefone deve ter o prefixo 81.");
        }

        usuarioService.save(cliente.getUsuario());

        cliente.setHabilitado(Boolean.TRUE);
        cliente.setVersao(1L);
        cliente.setDataCriacao(LocalDate.now());
        Cliente clienteSalvo = repository.save(cliente);

        emailService.enviarEmailConfirmacaoCadastroCliente(clienteSalvo);

        return clienteSalvo;
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente obterPorID(Long id) {
        Optional<Cliente> consulta = repository.findById(id);
        if (consulta.isPresent()) {
            return consulta.get();
        } else {
            throw new EntidadeNaoEncontradaException("Cliente", id);
        }
    }

    @Transactional
    public void update(Long id, Cliente clienteAlterado) {
        Cliente cliente = repository.findById(id).get();

        if (!telefoneComPrefixo81(clienteAlterado.getFoneCelular())
                || (clienteAlterado.getFoneFixo() != null && !telefoneComPrefixo81(clienteAlterado.getFoneFixo()))) {
            throw new TelefoneInvalidoException("O telefone deve ter o prefixo 81.");
        }

        cliente.setNome(clienteAlterado.getNome());
        cliente.setDataNascimento(clienteAlterado.getDataNascimento());
        cliente.setCpf(clienteAlterado.getCpf());
        cliente.setFoneCelular(clienteAlterado.getFoneCelular());
        cliente.setFoneFixo(clienteAlterado.getFoneFixo());

        cliente.setVersao(cliente.getVersao() + 1);
        repository.save(cliente);
    }

    @Transactional
    public void delete(Long id) {
        Cliente cliente = repository.findById(id).get();
        cliente.setHabilitado(Boolean.FALSE);
        cliente.setVersao(cliente.getVersao() + 1);

        repository.save(cliente);
    }

    @Transactional
    public EnderecoCliente adicionarEnderecoCliente(Long clienteId, EnderecoCliente endereco) {
        Cliente cliente = this.obterPorID(clienteId);

        endereco.setCliente(cliente);
        endereco.setHabilitado(Boolean.TRUE);
        enderecoClienteRepository.save(endereco);

        List<EnderecoCliente> listaEnderecoCliente = cliente.getEnderecos();

        if (listaEnderecoCliente == null) {
            listaEnderecoCliente = new ArrayList<EnderecoCliente>();
        }

        listaEnderecoCliente.add(endereco);
        cliente.setEnderecos(listaEnderecoCliente);
        cliente.setVersao(cliente.getVersao() + 1);
        repository.save(cliente);

        return endereco;
    }

    @Transactional
    public EnderecoCliente atualizarEnderecoCliente(Long id, EnderecoCliente enderecoAlterado) {
        EnderecoCliente endereco = enderecoClienteRepository.findById(id).get();
        endereco.setRua(enderecoAlterado.getRua());
        endereco.setNumero(enderecoAlterado.getNumero());
        endereco.setBairro(enderecoAlterado.getBairro());
        endereco.setCep(enderecoAlterado.getCep());
        endereco.setCidade(enderecoAlterado.getCidade());
        endereco.setUf(enderecoAlterado.getUf());
        endereco.setComplemento(enderecoAlterado.getComplemento());

        return enderecoClienteRepository.save(endereco);
    }

    @Transactional
    public void removerEnderecoCliente(Long id) {
        EnderecoCliente endereco = enderecoClienteRepository.findById(id).get();
        endereco.setHabilitado(Boolean.FALSE);
        enderecoClienteRepository.save(endereco);

        Cliente cliente = this.obterPorID(endereco.getCliente().getId());
        cliente.getEnderecos().remove(endereco);
        cliente.setVersao(cliente.getVersao() + 1);
        repository.save(cliente);
    }
}
