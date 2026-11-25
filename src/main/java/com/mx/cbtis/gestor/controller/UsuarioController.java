package com.mx.cbtis.gestor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mx.cbtis.gestor.modelo.Usuario;
import com.mx.cbtis.gestor.repository.UsuarioRepository;

@SuppressWarnings("java:S4684")
@RestController
public class UsuarioController {
	
	private static final String ROL_POR_DEFECTO = "Estudiante";

	@Autowired
	UsuarioRepository usuariotable;

	private String normalizarRolSeguro(String rolSolicitado) {
		if ("Docente".equalsIgnoreCase(rolSolicitado)) {
			return "Docente";
		}
		return ROL_POR_DEFECTO;
	}
	
	@PostMapping("/getUsuarioLogin")
	public Usuario getUsuarioLogin(@RequestBody Usuario userParam) {
		
		Optional<Usuario> optionaluser = usuariotable.findByCorreoPass(
				userParam.getCorreo_electronico(),
				userParam.getContrasena());
		
		Usuario user = new Usuario();
		
		if(optionaluser.isPresent()) {
			user = optionaluser.get();
		}else {
			user.setNombre("000");
		}
		
		return user;
	}
	
	@GetMapping("/buscarUsuario")
	public Usuario getUsuario(@RequestParam int id) {
		
		Usuario buscar = new Usuario();
		Optional<Usuario> usuario = usuariotable.findById(id);
		if(usuario.isPresent())
		{
			buscar = usuario.get();
		}
		
		return buscar;
	}
	
	@PostMapping("/addUsuario")
	public int addUsuario(@RequestBody Usuario usuario) {
		int flag = 0;
		if(usuario != null) {
			usuario.setId_usuario(null);
			usuario.setRol(normalizarRolSeguro(usuario.getRol()));
			usuariotable.save(usuario);
			flag = 1;
		}
		
		return flag;
	}
	
	@PostMapping("/updateUsuario")
	public int updateUsuario(@RequestBody Usuario usuario) {
		int flag = 0;
		if(usuario != null) {
			if (usuario.getId_usuario() != null) {
				Optional<Usuario> existente = usuariotable.findById(usuario.getId_usuario());
				if (existente.isPresent() && existente.get().getRol() != null) {
					usuario.setRol(existente.get().getRol());
				} else {
					usuario.setRol(normalizarRolSeguro(usuario.getRol()));
				}
			} else {
				usuario.setRol(normalizarRolSeguro(usuario.getRol()));
			}
			usuariotable.save(usuario);
			flag = 1;
		}
		
		return flag;
	}
	
	@DeleteMapping("/deleteUsuario")
	public int deleteUsuario(@RequestBody Usuario usuario) {
		int flag = 0;
		if(usuario != null) {
			usuariotable.delete(usuario);
			flag = 1;
		}
		
		return flag;
	}
	
	@PostMapping("/buscarUsuarios")
	public List<Usuario> getUsuarios(){
		List<Usuario> listaUsuarios = new ArrayList<>(); 
		usuariotable.findAll().forEach(listaUsuarios::add);
		return listaUsuarios;
	}		
}
