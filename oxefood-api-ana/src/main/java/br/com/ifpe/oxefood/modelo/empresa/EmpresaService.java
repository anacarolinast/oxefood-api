package br.com.ifpe.oxefood.modelo.empresa;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.oxefood.modelo.acesso.UsuarioService;
import jakarta.transaction.Transactional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public Empresa save(Empresa empresa) {

        usuarioService.save(empresa.getUsuario());
        empresa.setHabilitado(Boolean.TRUE);
        empresa.setVersao(1L);
        empresa.setDataCriacao(LocalDate.now());

        return repository.save(empresa);
    }

}