/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.servico;
import br.edu.utfpr.dto.ClienteDTO;
import br.edu.utfpr.dto.PaisDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Stefanie
 */
@RestController
public class ServicoCliente {
   
    private List<ClienteDTO> clientes;
    private list<PaisDTO> paises;
    
    public ServicoCliente(){
        
        paises = Stream.of(
            PaisDTO.builder().id(1).nome("Brasil").sigla("BR").codigoTelefone(55).build(),
            PaisDTO.builder().id(2).nome("Estados Unidos da América").sigla("EUA").codigoTelefone(33).build(),
            PaisDTO.builder().id(3).nome("Reino Unido").sigla("RU").codigoTelefone(44).build()
        ).collect(Collectors.toList());
         
        clientes = Stream.of(
            ClienteDTO.builder().id(1).nome("Maria").idade(19).telefone("33220000").limiteCredito(700).pais(paises.get(0)).build(),
            ClienteDTO.builder().id(1).nome("Theo").idade(35).telefone("22340101").limiteCredito(3500).pais(paises.get(1)).build()
        ).collect(Collectors.toList());
    }
    
    @GetMapping ("/servico/cliente")
    public ResponseEntity<List<ClienteDTO>> listar() {
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping ("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> listarPorId(@PathVariable int id) {
        Optional<ClienteDTO> clienteEncontrado = cliente.stream().filter(p -> p.getId() == id).findAny();

        return ResponseEntity.of(clienteEncontrado);
    }
    
    @PostMapping ("/servico/pais")
    public ResponseEntity<ClienteDTO> criar (@RequestBody ClienteDTO cliente) {

        cliente.setId(clientes.size() + 1);
        clientes.add(cliente);

        return ResponseEntity.status(201).body(cliente);
    }
    
    @DeleteMapping ("/servico/cliente/{id}")
    public ResponseEntity excluir (@PathVariable int id) {
        
        if (clientes.removeIf(cliente -> cliente.getId() == id))
            return ResponseEntity.noContent().build();

        else
            return ResponseEntity.notFound().build();
    }
    
    PutMapping ("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> alterar (@PathVariable int id, @RequestBody ClienteDTO cliente) {
        Optional<ClienteDTO> clienteExistente = clientes.stream().filter(p -> p.getId() == id).findAny();

        clienteExistente.ifPresent(c -> {
            try{
                c.setNome(cliente.getNome());
            } catch (NomeClienteMenor5CaracteresException e){
                return ResponseEntity.badRequest().build();
            }
            c.setIdade(cliente.getIdade());
            c.setTelefone(cliente.getTelefone());
            c.setLimiteCredito(cliente.getLimiteCredito());
            c.setPais(cliente.getPais());
        });

        return ResponseEntity.of(clienteExistente);
    }
    
}
