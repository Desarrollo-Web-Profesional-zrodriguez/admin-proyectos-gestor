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

import com.mx.cbtis.gestor.modelo.Tareas;
import com.mx.cbtis.gestor.repository.TareasRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("java:S4684")
@RestController
public class TareasController {
	
	private static final Logger log = LoggerFactory.getLogger(TareasController.class);

	@Autowired
	TareasRepository tareastable;
	
	@PostMapping("/getTarea")
	public List<Tareas> getTarea(@RequestBody Tareas id_proyecto) {
		return tareastable.findByIdProyecto(id_proyecto.getId_proyecto());
	}
	
	@GetMapping("/buscarTarea")
	public Tareas getTarea(@RequestParam int id_tarea) {
		
		Tareas buscar = new Tareas();
		Optional<Tareas> tarea = tareastable.findById(id_tarea);
		if(tarea.isPresent()) {
			buscar = tarea.get();
		}
		
		return buscar;
	}
	
	@PostMapping("/addTareas")
	public int addTareas(@RequestBody Tareas tareas) {
		
		int flag = 0;
		try {
			if(tareas != null) {
				// Si la fecha de vencimiento es cadena vacía, se asigna null para no violar la sintaxis de tipo DATE en PostgreSQL
				if(tareas.getFecha_vencimiento() != null && tareas.getFecha_vencimiento().trim().isEmpty()) {
					tareas.setFecha_vencimiento(null);
				}
				tareastable.save(tareas);
				flag = 1;
			}
		} catch (Exception e) {
			log.error("Error controlado al guardar tarea en BD: {}", e.getMessage());
			flag = 0;
		}
		
		return flag;
	}
	
	@PostMapping("/updateTareas")
	public int updateTareas(@RequestBody Tareas tareas) {
		
		int flag = 0;
		if(tareas != null) {
			tareastable.save(tareas);
			flag = 1;
		}
		
		return flag;
	}
	
	@DeleteMapping("/deleteTareas")
	public int deleteTareas(@RequestBody Tareas tareas) {
		
		int flag = 0;
		if(tareas != null) {
			tareastable.delete(tareas);
			flag = 1;
		}
		
		return flag;
	}
	
	@PostMapping("/buscarTareas")
	public List<Tareas> getTareas(){
		
		List<Tareas> listaTareas = new ArrayList<>();
		tareastable.findAll().forEach(listaTareas :: add);
		return listaTareas;
	}

}
