package com.giuli.api_financeira.repositories;


import com.giuli.api_financeira.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
