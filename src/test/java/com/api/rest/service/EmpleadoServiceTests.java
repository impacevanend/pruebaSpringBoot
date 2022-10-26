package com.api.rest.service;

import com.api.rest.exception.ResourceNotFoundException;
import com.api.rest.model.Empleado;
import com.api.rest.repository.EmpleadoRepository;
import com.api.rest.service.impl.EmpleadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTests {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks //Para inyectarce dentro del @Mock
    private EmpleadoServiceImpl empleadoService;

    Empleado empleado;

    @BeforeEach
    void setup(){
        empleado = Empleado.builder()
                .id(1L)
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();
    }

    @DisplayName("Test para guardar un empleado")
    @Test
    void testGuardarEmpleado(){
        //Given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.empty());
        given(empleadoRepository.save(empleado))
                .willReturn(empleado);
        //When
        Empleado empleadoGuardado = empleadoService.saveEmpleado(empleado);
        //Then
        assertThat(empleadoGuardado).isNotNull();


    }

    @DisplayName("Test para guardar un empleado con ThrowException")
    @Test
    void testGuardarEmpleadoConThrowException(){
        //Given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.of(empleado));

        //When
        assertThrows(ResourceNotFoundException.class, ()->{
           empleadoService.saveEmpleado(empleado);
        });

        //Then
        verify(empleadoRepository, never()).save(any(Empleado.class));


    }

    @DisplayName("Test para listar a los empleados")
    @Test
    void listarEmpleados(){
        //given
        Empleado empleado1 = Empleado.builder()
                .id(2l)
                .nombre("Jule")
                .apellido("Oliva")
                .email("j2@gmail.com")
                .build();

        given(empleadoRepository.findAll())
                .willReturn(List.of(empleado, empleado1));

        //when
        List<Empleado> empleados = empleadoService.getAllEmpleados();
        //then
        assertThat(empleados).isNotNull();
        assertThat(empleados).size().isEqualTo(2);
    }

    @DisplayName("Test para retornar una lista vacia")
    @Test
    void testListarColeccionEmpleadosVacia(){
        //Given
        Empleado empleado1 = Empleado.builder()
                .id(2l)
                .nombre("Jule")
                .apellido("Oliva")
                .email("j2@gmail.com")
                .build();
        given(empleadoRepository.findAll())
                .willReturn(Collections.emptyList());
        //When
        List<Empleado> listaEmpleado = empleadoService.getAllEmpleados();
        //Then
        assertThat(listaEmpleado).isEmpty();
        assertThat(listaEmpleado.size()).isEqualTo(0);
    }

    @DisplayName("Test para obtener un empleado por id")
    @Test
    void testObtenerEmpleadoPorId(){
        //given
        given(empleadoRepository.findById(1L)).willReturn(Optional.of(empleado));
        //when
        Empleado empleadoGuardado = empleadoService.getEmpleadoById(empleado.getId()).get();

        //then
        assertThat(empleadoGuardado).isNotNull();
    }

    @DisplayName("Test para actualizar un empleado")
    @Test
    void actualizarEmpleado(){
        //given
        given(empleadoRepository.save(empleado)).willReturn(empleado);
        empleado.setEmail("chr2@gmail.com");
        empleado.setNombre("Christian Raul");
        //when
        Empleado empleadoActualizado = empleadoService.updateEmpleado(empleado);
        //then
        assertThat(empleadoActualizado.getEmail()).isEqualTo("chr2@gmail.com");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Christian Raul");
    }

    @DisplayName("Test para eliminar un empleado")
    @Test
    void eliminarEmpleado(){
        //given
        long empleadoId = 1;
        willDoNothing().given(empleadoRepository).deleteById(empleadoId);
        //when
        empleadoService.deleteEmpleado(empleadoId);
        //then
        verify(empleadoRepository,times(1)).deleteById(empleadoId);

    }
}
