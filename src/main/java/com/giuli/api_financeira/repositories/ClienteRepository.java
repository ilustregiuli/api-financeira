package com.giuli.api_financeira.repositories;

import com.giuli.api_financeira.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClienteRepository extends JpaRepository<Cliente, Long>{
}
