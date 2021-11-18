package com.leandroninja.livros.service;

import com.leandroninja.livros.model.Livro;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LivroService {

    private final List<Livro> livros = new ArrayList<>();
    private final AtomicLong contador = new AtomicLong(1);

    public LivroService() {
        livros.add(new Livro(contador.getAndIncrement(), "Clean Code", "Robert C. Martin", 2008, "978-0132350884"));
        livros.add(new Livro(contador.getAndIncrement(), "The Pragmatic Programmer", "David Thomas", 2019, "978-0135957059"));
        livros.add(new Livro(contador.getAndIncrement(), "Design Patterns", "Gang of Four", 1994, "978-0201633610"));
    }

    public List<Livro> listarTodos() {
        return new ArrayList<>(livros);
    }

    public Optional<Livro> buscarPorId(Long id) {
        return livros.stream().filter(l -> l.getId().equals(id)).findFirst();
    }

    public Livro salvar(Livro livro) {
        livro.setId(contador.getAndIncrement());
        livros.add(livro);
        return livro;
    }

    public Optional<Livro> atualizar(Long id, Livro dados) {
        return buscarPorId(id).map(livro -> {
            livro.setTitulo(dados.getTitulo());
            livro.setAutor(dados.getAutor());
            livro.setAnoPublicacao(dados.getAnoPublicacao());
            livro.setIsbn(dados.getIsbn());
            return livro;
        });
    }

    public boolean deletar(Long id) {
        return livros.removeIf(l -> l.getId().equals(id));
    }
}
