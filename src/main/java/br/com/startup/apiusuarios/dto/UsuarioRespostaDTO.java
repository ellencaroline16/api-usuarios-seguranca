package br.com.startup.apiusuarios.dto;

import br.com.startup.apiusuarios.model.Perfil;
import br.com.startup.apiusuarios.model.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRespostaDTO {

    private Long id;
    private String nome;
    private String email;
    private Perfil perfil;

    public UsuarioRespostaDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.perfil = usuario.getPerfil();
    }
}