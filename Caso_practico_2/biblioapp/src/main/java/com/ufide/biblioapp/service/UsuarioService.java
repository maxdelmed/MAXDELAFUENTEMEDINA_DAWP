package com.ufide.biblioapp.service;

import com.ufide.biblioapp.entity.Usuario;
import com.ufide.biblioapp.enums.Rol;
import com.ufide.biblioapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        Rol rol;
        try {
            rol = Rol.valueOf(usuario.getRol());
        } catch (IllegalArgumentException ex) {
            throw new UsernameNotFoundException("Rol no valido para el usuario");
        }

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + rol.name())))
                .build();
    }

    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    // ==========================================================
    // CASO PRACTICO 2 - BONUS (CRUD completo de Usuarios):
    // Si vas a implementar el bonus, agrega aca los metodos
    // listar/guardar/eliminar usuarios, con la misma logica de
    // validarRol(...) que viste en la Semana 12 (UsuarioService
    // de cursosapp).
    // ==========================================================
    public List<Usuario> listarLectores() {
        return usuarioRepository.findByRolOrderByNombreCompletoAsc(Rol.LECTOR.name());
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}
