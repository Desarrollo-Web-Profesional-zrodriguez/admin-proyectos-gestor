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

import com.mx.cbtis.gestor.modelo.Comentarios;
import com.mx.cbtis.gestor.repository.ComentariosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("java:S4684")
@RestController
public class ComentariosController {
	
	private static final Logger log = LoggerFactory.getLogger(ComentariosController.class);

	@Autowired
	ComentariosRepository comentariostable;
	
	@PostMapping("/getComentario")
	public List<Comentarios> getComentario(@RequestBody Comentarios idComentario){
		return comentariostable.findByTarea(idComentario.getId_tarea());
	}
	
	@GetMapping("/buscarComentario")
	public Comentarios getComentario(@RequestParam int id) {
		
		Comentarios buscar = new Comentarios();
		Optional<Comentarios> comentario = comentariostable.findById(id);
		if(comentario.isPresent()) {
			buscar = comentario.get();
		}
		
		return buscar;
	}
	
	@PostMapping("/addComentario")
	public int addComentario(@RequestBody Comentarios comentario) {
		
		int flag = 0;
		try {
			if(comentario != null) {
				if(comentario.getFecha_hora() == null || comentario.getFecha_hora().trim().isEmpty()) {
					java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
					comentario.setFecha_hora(ahora.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				}
				comentariostable.save(comentario);
				flag = 1;
			}
		} catch (Exception e) {
			log.error("Error controlado al guardar comentario en BD: {}", e.getMessage());
			flag = 0;
		}
		
		return flag;
	}
	
	@PostMapping("/updateComentario")
	public int updateComentario(@RequestBody Comentarios comentario) {
		
		int flag = 0;
		if(comentario != null) {
			comentariostable.save(comentario);
			flag = 1;
		}
		
		return flag;
	}
	
	@DeleteMapping("/deleteComentario")
	public int deleteComentario(@RequestBody Comentarios comentario) {
		
		int flag = 0;
		if(comentario != null) {
			comentariostable.delete(comentario);
			flag = 1;
		}
		
		return flag;
	}
	
	@PostMapping("/buscarComentarios")
	public List<Comentarios> getComentarios(){
		
		List<Comentarios> listaComentarios = new ArrayList<>();
		comentariostable.findAll().forEach(listaComentarios :: add);
		return listaComentarios;
	}

}
