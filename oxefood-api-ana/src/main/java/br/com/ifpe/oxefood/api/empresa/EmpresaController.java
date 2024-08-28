package br.com.ifpe.oxefood.api.empresa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.oxefood.modelo.acesso.Usuario;
import br.com.ifpe.oxefood.modelo.empresa.Empresa;
import br.com.ifpe.oxefood.modelo.empresa.EmpresaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresa")
@CrossOrigin
public class EmpresaController {

	@Autowired
	private EmpresaService empresaService;

	@PostMapping
	public ResponseEntity<Empresa> save(@RequestBody @Valid EmpresaRequest request) {

		Empresa empresa = request.build();

		if (request.getPerfil() != null && !"".equals(request.getPerfil())) {

			if (request.getPerfil().equals("EMPRESA_USER")) {

				empresa.getUsuario().getRoles().add(Usuario.ROLE_EMPRESA_USER);

			} else if (request.getPerfil().equals("EMPRESA_ADMIN")) {

				empresa.getUsuario().getRoles().add(Usuario.ROLE_EMPRESA_ADMIN);
			}
		}

		Empresa empresaCriada = empresaService.save(empresa);
		return new ResponseEntity<Empresa>(empresaCriada, HttpStatus.CREATED);
	}

}