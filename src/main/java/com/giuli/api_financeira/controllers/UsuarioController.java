package com.giuli.api_financeira.controllers;



import com.giuli.api_financeira.dtos.requests.CreateUsuarioRequest;
import com.giuli.api_financeira.entities.Empresa;
import com.giuli.api_financeira.entities.Usuario;
import com.giuli.api_financeira.repositories.EmpresaRepository;
import com.giuli.api_financeira.repositories.UsuarioRepository;
import com.giuli.api_financeira.security.UserContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    public UsuarioController(UsuarioRepository usuarioRepository, EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
    }

    @PostMapping
    public String criarUsuario(@RequestBody CreateUsuarioRequest usuarioRequest) {

        usuarioRepository.findByEmail(usuarioRequest.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException("Email já cadastrado!");
                });

        Long empresaId = UserContext.getEmpresaId();

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada!"));

        Usuario usuario = new Usuario(
                usuarioRequest.getNome(),
                usuarioRequest.getEmail(),
                usuarioRequest.getSenha(),
                empresa
        );

        usuarioRepository.save(usuario);

        return "Usuário criado com sucesso";

    }
}
