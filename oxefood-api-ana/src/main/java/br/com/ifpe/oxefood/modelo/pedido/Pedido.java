package br.com.ifpe.oxefood.modelo.pedido;

import java.time.LocalDate;
import org.hibernate.annotations.SQLRestriction;
import br.com.ifpe.oxefood.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Pedido")
@SQLRestriction("habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido extends EntidadeAuditavel {
  
   @Column
   private LocalDate dataPedido;

   @Column
   private String cliente;

   @Column
   private String produto;

   @Column
   private int quantidadeProdutos;

   @Column
   private String rua;

   @Column
   private String numero;

   @Column
   private String bairro;

   @Column
   private String cidade;

   @Column
   private String cep;

   @Column
   private String uf;

   @Column
   private String complemento;

   @Column
   private String status;

   @Column
   private double valorTotal;
}
