package com.giuli.api_financeira.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Empresa {

    @OneToMany(mappedBy = "empresa")
    private List<Usuario> usuarios;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Usuario> getUsuarios() {
        return this.usuarios;
    }
}
