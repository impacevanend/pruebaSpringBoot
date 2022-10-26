package com.api.rest.repository;
import static org.assertj.core.api.Assertions.assertThat;

import com.api.rest.model.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

//Prueba componentes de la capa de persistencia(Etindades y repositorios )
@DataJpaTest
public class EmpleadoRepositoryTests {
    //Prueba de integración -> validar el correcto funcionamiento de todas las pruebas unitarias de esta clase.
    @Autowired
    private  EmpleadoRepository empleadoRepository;

    private Empleado empleado;

    @BeforeEach
    void setup(){
        empleado = Empleado.builder()
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();
    }

    @DisplayName("Test para guardar un empleado")
    @Test
    void testGuardarEmpleado(){
        //BDD
        //given - dado o condición previa o configuración.
        Empleado empleado1 = Empleado.builder()
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@gmail.com")
                .build();
        //when - acción o el comportamiento que vamos a probar.
        Empleado empleadoGuardado = empleadoRepository.save(empleado1);
        //then - verificar la salida.
        assertThat(empleadoGuardado).isNotNull();
        assertThat(empleadoGuardado.getId()).isGreaterThan(0);
    }

    @DisplayName("Test para listar a los empleado")
    @Test
    void testListarEmpleados(){
        //given -> dado los siguientes empleados guardados:
        Empleado empleado1 = Empleado.builder()
                .nombre("Juelen")
                .apellido("Oliva")
                .email("j2@gmail.com")
                .build();
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado);

        //when-> cuando listamos los empleados
        List<Empleado> listaEmpleados = empleadoRepository.findAll();

        //Then (verificación de la salida)
        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados).size().isEqualTo(2);
    }

    //Listar un empleado por id.
    @DisplayName("Test para obtener un empleado por id")
    @Test
    void testObtenerEmpleadoPorId(){

        //Given
        empleadoRepository.save(empleado);

        //When - comportamiento o acción que vamos a probar
        Empleado empleadoBD = empleadoRepository.findById(empleado.getId()).get();

        //Then
        assertThat(empleadoBD).isNotNull();

    }

    @DisplayName("Test para actualizar un empleado")
    @Test
    void actualizarEmpleado(){
        //Given
        empleadoRepository.save(empleado);

        //When
        Empleado empleadoGuardado = empleadoRepository.findById(empleado.getId()).get();
        empleadoGuardado.setEmail("c232@gmail.com");
        empleadoGuardado.setNombre("Christian Raul");
        empleadoGuardado.setApellido("Ramirez Cucitini");
        Empleado empleadoActualizado = empleadoRepository.save(empleadoGuardado);

        //Then
        assertThat(empleadoActualizado.getEmail()).isEqualTo("c232@gmail.com");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Christian Raul");
    }

    @DisplayName("Test para eliminar un empleado")
    @Test
    void testEliminarEmpleado(){
        //Give
        empleadoRepository.save(empleado);

        //When
        empleadoRepository.deleteById(empleado.getId());
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(empleado.getId());

        //Then
        assertThat(empleadoOptional).isEmpty();

    }
}
