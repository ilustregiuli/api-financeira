package com.giuli.api_financeira.controllers;


import com.giuli.api_financeira.dtos.RegisterRequest;
import com.giuli.api_financeira.dtos.reponses.ApiResponse;
import com.giuli.api_financeira.dtos.requests.LoginRequest;
import com.giuli.api_financeira.entities.Empresa;
import com.giuli.api_financeira.entities.Usuario;
import com.giuli.api_financeira.repositories.EmpresaRepository;
import com.giuli.api_financeira.repositories.UsuarioRepository;
import com.giuli.api_financeira.security.JwtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepository,
                          JwtService jwtService, EmpresaRepository empresaRepository ) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.empresaRepository = empresaRepository;
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequest request) {

        System.out.println(request.getEmail());
        System.out.println(request.getSenha());

        Usuario usuario = usuarioRepository
                .findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("Usuário não encontrado."));

        if(!usuario.getSenha().equals(request.getSenha())) {
            throw new RuntimeException("Senha inválida.");
        }

        String token = jwtService.gerarToken(
                usuario.getId(),
                usuario.getEmpresa().getId()
        );

        return new ApiResponse<>(200, token);
    }

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody RegisterRequest request) {

        usuarioRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException("Email já cadastrado.");
                });

        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada."));

        Usuario usuario = new Usuario(
            request.getNome(),
            request.getEmail(),
            request.getSenha(),
            empresa);

        usuarioRepository.save(usuario);

        return new ApiResponse<>(201, "Usuário criado com sucesso");
    }
}
