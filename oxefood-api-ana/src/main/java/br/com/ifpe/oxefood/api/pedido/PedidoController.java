package br.com.ifpe.oxefood.api.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import br.com.ifpe.oxefood.modelo.pedido.Pedido;
import br.com.ifpe.oxefood.modelo.pedido.PedidoService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/pedido")
@CrossOrigin
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(
       summary = "Serviço responsável por salvar um pedido no sistema.",
       description = "Exemplo de descrição de um endpoint responsável por inserir um pedido no sistema."
   )

    @PostMapping
    public ResponseEntity<Pedido> save(@RequestBody PedidoRequest request) {

        Pedido pedido = pedidoService.save(request.build());
        return new ResponseEntity<Pedido>(pedido, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Pedido obterPorID(@PathVariable Long id) {
        return pedidoService.obterPorID(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> update(@PathVariable("id") Long id, @RequestBody PedidoRequest request) {

        pedidoService.update(id, request.build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        pedidoService.delete(id);
        return ResponseEntity.ok().build();
    }
}
