package br.com.startup.apiusuarios.controller;

import br.com.startup.apiusuarios.dto.UsuarioAtualizacaoDTO;
import br.com.startup.apiusuarios.dto.UsuarioCadastroDTO;
import br.com.startup.apiusuarios.dto.UsuarioRespostaDTO;
import br.com.startup.apiusuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioRespostaDTO> cadastrar(@Valid @RequestBody UsuarioCadastroDTO dto) {
        UsuarioRespostaDTO resposta = usuarioService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @GetMapping
    public ResponseEntity<List<UsuarioRespostaDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR') or @usuarioService.buscarPorId(#id).email == authentication.name")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody UsuarioAtualizacaoDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizar(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}