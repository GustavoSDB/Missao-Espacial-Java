package com.missaoespacial.api.controller;

import com.missaoespacial.api.model.Usuario;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private List<Usuario> usuarios = new ArrayList<>();

    @GetMapping
    public List<Usuario> listar() {
        return usuarios;
    }

    @PostMapping
    public String criar(@RequestBody Usuario usuario) {
        usuarios.add(usuario);
        return "Usuário criado!";
    }
}