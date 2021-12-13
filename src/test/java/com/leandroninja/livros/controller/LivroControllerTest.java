package com.leandroninja.livros.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandroninja.livros.model.Livro;
import com.leandroninja.livros.service.LivroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivroController.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LivroService service;

    @Test
    @DisplayName("GET /api/livros retorna lista de livros")
    void deveListarTodosOsLivros() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(
                new Livro(1L, "Clean Code", "Robert C. Martin", 2008, "978-0132350884")
        ));

        mockMvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Clean Code"))
                .andExpect(jsonPath("$[0].autor").value("Robert C. Martin"));
    }

    @Test
    @DisplayName("GET /api/livros/{id} retorna livro existente")
    void deveBuscarLivroPorId() throws Exception {
        Livro livro = new Livro(1L, "Clean Code", "Robert C. Martin", 2008, "978-0132350884");
        when(service.buscarPorId(1L)).thenReturn(Optional.of(livro));

        mockMvc.perform(get("/api/livros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Clean Code"));
    }

    @Test
    @DisplayName("GET /api/livros/{id} retorna 404 para livro inexistente")
    void deveRetornar404ParaLivroInexistente() throws Exception {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/livros/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/livros cria novo livro")
    void deveCriarNovoLivro() throws Exception {
        Livro novo = new Livro(null, "Refactoring", "Martin Fowler", 2018, "978-0134757599");
        Livro salvo = new Livro(4L, "Refactoring", "Martin Fowler", 2018, "978-0134757599");
        when(service.salvar(any())).thenReturn(salvo);

        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.titulo").value("Refactoring"));
    }

    @Test
    @DisplayName("POST /api/livros retorna 400 para dados inválidos")
    void deveRetornar400ParaDadosInvalidos() throws Exception {
        Livro invalido = new Livro(null, "", "", null, "");

        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/livros/{id} remove livro existente")
    void deveDeletarLivro() throws Exception {
        when(service.deletar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/livros/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /api/livros/{id} atualiza livro existente")
    void deveAtualizarLivro() throws Exception {
        Livro atualizado = new Livro(1L, "Clean Code 2", "Robert C. Martin", 2024, "978-0132350884");
        when(service.atualizar(eq(1L), any())).thenReturn(Optional.of(atualizado));

        mockMvc.perform(put("/api/livros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Clean Code 2"));
    }
}
