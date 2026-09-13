package br.com.startup.apiusuarios.service;

import br.com.startup.apiusuarios.dto.LoginDTO;
import br.com.startup.apiusuarios.dto.TokenDTO;
import br.com.startup.apiusuarios.dto.UsuarioAtualizacaoDTO;
import br.com.startup.apiusuarios.dto.UsuarioCadastroDTO;
import br.com.startup.apiusuarios.dto.UsuarioRespostaDTO;
import br.com.startup.apiusuarios.exception.EmailJaCadastradoException;
import br.com.startup.apiusuarios.exception.UsuarioNaoEncontradoException;
import br.com.startup.apiusuarios.model.Usuario;
import br.com.startup.apiusuarios.repository.UsuarioRepository;
import br.com.startup.apiusuarios.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public UsuarioRespostaDTO cadastrar(UsuarioCadastroDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("Já existe um usuário com esse e-mail");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setPerfil(dto.getPerfil());

        Usuario salvo = usuarioRepository.save(usuario);
        return new UsuarioRespostaDTO(salvo);
    }

    public TokenDTO login(LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtUtil.gerarToken(usuario.getEmail(), usuario.getPerfil().name(), usuario.getId());
        return new TokenDTO(token);
    }

    public List<UsuarioRespostaDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioRespostaDTO::new)
                .collect(Collectors.toList());
    }

    public UsuarioRespostaDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com id: " + id));
        return new UsuarioRespostaDTO(usuario);
    }

    public UsuarioRespostaDTO atualizar(Long id, UsuarioAtualizacaoDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com id: " + id));

        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("Já existe um usuário com esse e-mail");
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());

        Usuario atualizado = usuarioRepository.save(usuario);
        return new UsuarioRespostaDTO(atualizado);
    }

    public void excluir(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado com id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}