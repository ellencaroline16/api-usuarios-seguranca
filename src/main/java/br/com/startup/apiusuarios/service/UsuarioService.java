package br.com.startup.apiusuarios.service;

import br.com.startup.apiusuarios.dto.UsuarioCadastroDTO;
import br.com.startup.apiusuarios.dto.UsuarioRespostaDTO;
import br.com.startup.apiusuarios.exception.EmailJaCadastradoException;
import br.com.startup.apiusuarios.model.Usuario;
import br.com.startup.apiusuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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
}