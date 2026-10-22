package com.mx.cbtis.gestor.modelo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mx.cbtis.gestor.Ejercicio5MyApiGestorApplication;
import com.mx.cbtis.gestor.controller.AsignacionesController;
import com.mx.cbtis.gestor.controller.ComentariosController;
import com.mx.cbtis.gestor.controller.ProyectosController;
import com.mx.cbtis.gestor.controller.TareasController;
import com.mx.cbtis.gestor.controller.UsuarioController;
import com.mx.cbtis.gestor.repository.AsignacionesRepository;
import com.mx.cbtis.gestor.repository.ComentariosRepository;
import com.mx.cbtis.gestor.repository.ProtectosRepository;
import com.mx.cbtis.gestor.repository.TareasRepository;
import com.mx.cbtis.gestor.repository.UsuarioRepository;

public class ModelosYCoberturaExtraTest {

	@Test
	public void testApplicationConstructor() {
		Ejercicio5MyApiGestorApplication app = new Ejercicio5MyApiGestorApplication();
		assertNotNull(app);
	}

	@Test
	public void testUsuario() {
		Usuario u1 = new Usuario();
		assertNotNull(u1);
		
		Usuario u2 = new Usuario(1, "Juan", "juan@test.com", "pass123", "ADMIN", "avatar.png");
		assertEquals(1, u2.getId_usuario());
		assertEquals("Juan", u2.getNombre());
		assertEquals("juan@test.com", u2.getCorreo_electronico());
		assertEquals("pass123", u2.getContrasena());
		assertEquals("ADMIN", u2.getRol());
		assertEquals("avatar.png", u2.getImagen_usuario());
		
		u1.setId_usuario(2);
		u1.setNombre("Pedro");
		u1.setCorreo_electronico("pedro@test.com");
		u1.setContrasena("abc");
		u1.setRol("DEV");
		u1.setImagen_usuario("dev.png");
		
		assertEquals(2, u1.getId_usuario());
		assertEquals("Pedro", u1.getNombre());
		assertEquals("pedro@test.com", u1.getCorreo_electronico());
		assertEquals("abc", u1.getContrasena());
		assertEquals("DEV", u1.getRol());
		assertEquals("dev.png", u1.getImagen_usuario());
	}

	@Test
	public void testAsignaciones() {
		Asignaciones a1 = new Asignaciones();
		assertNotNull(a1);
		
		Asignaciones a2 = new Asignaciones(10, 1, 5, "2026-10-21");
		assertEquals(10, a2.getId_asignacion());
		assertEquals(1, a2.getId_usuario());
		assertEquals(5, a2.getId_tarea());
		assertEquals("2026-10-21", a2.getFecha_asignacion());
		
		a1.setId_asignacion(20);
		a1.setId_usuario(2);
		a1.setId_tarea(6);
		a1.setFecha_asignacion("2026-10-22");
		
		assertEquals(20, a1.getId_asignacion());
		assertEquals(2, a1.getId_usuario());
		assertEquals(6, a1.getId_tarea());
		assertEquals("2026-10-22", a1.getFecha_asignacion());
	}

	@Test
	public void testComentarios() {
		Comentarios c1 = new Comentarios();
		assertNotNull(c1);
		
		Comentarios c2 = new Comentarios(100, "user1", 5, "Buen trabajo", "2026-10-21 12:00:00");
		assertEquals(100, c2.getId_comentario());
		assertEquals("user1", c2.getId_usuario());
		assertEquals(5, c2.getId_tarea());
		assertEquals("Buen trabajo", c2.getTexto_comentario());
		assertEquals("2026-10-21 12:00:00", c2.getFecha_hora());
		
		c1.setId_comentario(200);
		c1.setId_usuario("user2");
		c1.setId_tarea(6);
		c1.setTexto_comentario("Revisado");
		c1.setFecha_hora("2026-10-22 10:00:00");
		
		assertEquals(200, c1.getId_comentario());
		assertEquals("user2", c1.getId_usuario());
		assertEquals(6, c1.getId_tarea());
		assertEquals("Revisado", c1.getTexto_comentario());
		assertEquals("2026-10-22 10:00:00", c1.getFecha_hora());
	}

	@Test
	public void testProyectos() {
		Proyectos p1 = new Proyectos();
		assertNotNull(p1);
		
		Proyectos p2 = new Proyectos(1, "Proyecto A", "Desc A", "2026-01-01", "2026-12-31", "ACTIVO", "fondo.jpg");
		assertEquals(1, p2.getId_proyecto());
		assertEquals("Proyecto A", p2.getNombre());
		assertEquals("Desc A", p2.getDescripcion());
		assertEquals("2026-01-01", p2.getFecha_inicio());
		assertEquals("2026-12-31", p2.getFecha_finalizacion());
		assertEquals("ACTIVO", p2.getEstado_proyecto());
		assertEquals("fondo.jpg", p2.getFondo());
		
		p1.setId_proyecto(2);
		p1.setNombre("Proyecto B");
		p1.setDescripcion("Desc B");
		p1.setFecha_inicio("2026-02-01");
		p1.setFecha_finalizacion("2026-11-30");
		p1.setEstado_proyecto("FINALIZADO");
		p1.setFondo("fondo2.jpg");
		
		assertEquals(2, p1.getId_proyecto());
		assertEquals("Proyecto B", p1.getNombre());
		assertEquals("Desc B", p1.getDescripcion());
		assertEquals("2026-02-01", p1.getFecha_inicio());
		assertEquals("2026-11-30", p1.getFecha_finalizacion());
		assertEquals("FINALIZADO", p1.getEstado_proyecto());
		assertEquals("fondo2.jpg", p1.getFondo());
	}

	@Test
	public void testTareas() {
		Tareas t1 = new Tareas();
		assertNotNull(t1);
		
		Tareas t2 = new Tareas(1, "Tarea 1", "Desc T1", "2026-10-31", "TODO", "PENDIENTE", 10);
		assertEquals(1, t2.getId_tarea());
		assertEquals("Tarea 1", t2.getNombre());
		assertEquals("Desc T1", t2.getDescripcion());
		assertEquals("2026-10-31", t2.getFecha_vencimiento());
		assertEquals("TODO", t2.getLista());
		assertEquals("PENDIENTE", t2.getEstado());
		assertEquals(10, t2.getId_proyecto());
		
		t1.setId_tarea(2);
		t1.setNombre("Tarea 2");
		t1.setDescripcion("Desc T2");
		t1.setFecha_vencimiento("2026-11-15");
		t1.setLista("DONE");
		t1.setEstado("COMPLETADA");
		t1.setId_proyecto(20);
		
		assertEquals(2, t1.getId_tarea());
		assertEquals("Tarea 2", t1.getNombre());
		assertEquals("Desc T2", t1.getDescripcion());
		assertEquals("2026-11-15", t1.getFecha_vencimiento());
		assertEquals("DONE", t1.getLista());
		assertEquals("COMPLETADA", t1.getEstado());
		assertEquals(20, t1.getId_proyecto());
	}

	@Test
	public void testTareasController_RamasExtra() {
		TareasController controller = new TareasController();
		TareasRepository repo = Mockito.mock(TareasRepository.class);
		
		try {
			java.lang.reflect.Field field = TareasController.class.getDeclaredField("tareastable");
			field.setAccessible(true);
			field.set(controller, repo);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		// Fecha vencimiento vacía -> se asigna null
		Tareas t = new Tareas();
		t.setFecha_vencimiento("   ");
		int res = controller.addTareas(t);
		assertEquals(1, res);
		assertNull(t.getFecha_vencimiento());

		// Exception en save -> captura error y devuelve 0
		doThrow(new RuntimeException("DB error")).when(repo).save(any(Tareas.class));
		int resError = controller.addTareas(new Tareas());
		assertEquals(0, resError);
	}

	@Test
	public void testComentariosController_RamasExtra() {
		ComentariosController controller = new ComentariosController();
		ComentariosRepository repo = Mockito.mock(ComentariosRepository.class);
		
		try {
			java.lang.reflect.Field field = ComentariosController.class.getDeclaredField("comentariostable");
			field.setAccessible(true);
			field.set(controller, repo);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		// Fecha hora nula -> se autogenera fecha_hora
		Comentarios cNull = new Comentarios();
		cNull.setFecha_hora(null);
		int res1 = controller.addComentario(cNull);
		assertEquals(1, res1);
		assertNotNull(cNull.getFecha_hora());

		// Fecha hora en blanco -> se autogenera fecha_hora
		Comentarios cBlank = new Comentarios();
		cBlank.setFecha_hora("   ");
		int res2 = controller.addComentario(cBlank);
		assertEquals(1, res2);
		assertNotNull(cBlank.getFecha_hora());

		// Exception en save -> captura error y devuelve 0
		doThrow(new RuntimeException("DB error")).when(repo).save(any(Comentarios.class));
		int resError = controller.addComentario(new Comentarios());
		assertEquals(0, resError);
	}

	@Test
	public void testProyectosController_RamasExtra() {
		ProyectosController controller = new ProyectosController();
		ProtectosRepository repo = Mockito.mock(ProtectosRepository.class);
		
		try {
			java.lang.reflect.Field field = ProyectosController.class.getDeclaredField("proyectostable");
			field.setAccessible(true);
			field.set(controller, repo);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		when(repo.findById(999)).thenReturn(Optional.empty());
		Proyectos vacio = controller.getProyecto(999);
		assertNull(vacio.getId_proyecto());

		assertEquals(0, controller.addProyecto(null));
		assertEquals(0, controller.updateProyecto(null));
		assertEquals(0, controller.deleteProyecto(null));
	}

	@Test
	public void testAsignacionesController_RamasExtra() {
		AsignacionesController controller = new AsignacionesController();
		AsignacionesRepository repo = Mockito.mock(AsignacionesRepository.class);
		
		try {
			java.lang.reflect.Field field = AsignacionesController.class.getDeclaredField("asignacionestable");
			field.setAccessible(true);
			field.set(controller, repo);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		when(repo.findById(999)).thenReturn(Optional.empty());
		Asignaciones vacio = controller.getAsignacion(999);
		assertNull(vacio.getId_asignacion());

		assertEquals(0, controller.addAsignacion(null));
		assertEquals(0, controller.updateAsignacion(null));
		assertEquals(0, controller.deleteAsignacion(null));
	}
}
