package com.giuli.api_financeira.controllers;


import com.giuli.api_financeira.dtos.LancamentoDTO;
import com.giuli.api_financeira.dtos.ResumoMensalDTO;
import com.giuli.api_financeira.dtos.SaldoDTO;
import com.giuli.api_financeira.dtos.reponses.ApiResponse;
import com.giuli.api_financeira.dtos.reponses.PageResponse;
import com.giuli.api_financeira.entities.Lancamento;
import com.giuli.api_financeira.entities.TipoLancamento;
import com.giuli.api_financeira.requests.ResumoMensalRequest;
import com.giuli.api_financeira.services.LancamentoService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

    private final LancamentoService lancamentoService;

    public LancamentoController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @GetMapping
    public ApiResponse<PageResponse<LancamentoDTO>> buscar(

            @RequestParam(required = false)
            String descricao,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataFim,

            @RequestParam(required = false) TipoLancamento tipo,

            @RequestParam(required = false)
            BigDecimal valorMin,

            @RequestParam(required = false)
            BigDecimal valorMax,

            @PageableDefault(sort = "data", direction = Sort.Direction.DESC)
            Pageable pageable) {

        PageResponse<LancamentoDTO> dados =
                    lancamentoService.buscarComFiltros(
                            descricao,
                            dataInicio,
                            dataFim,
                            tipo,
                            valorMin,
                            valorMax,
                            pageable);

        return new ApiResponse<>(200, dados);
    }

    @GetMapping("/saldo")
    public SaldoDTO saldo(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataFim


    ) {
        return lancamentoService.calcularSaldo(dataInicio, dataFim);
    }

    @GetMapping("/resumo-anual")
    public ApiResponse<List<ResumoMensalDTO>> resumoAnual(
            @RequestParam (required = false) Integer ano) {
        List<ResumoMensalDTO> dados = lancamentoService.resumoAnual(ano);

        return new ApiResponse<>(200, dados);
    }

    @GetMapping("/resumo-mensal")
    public ApiResponse<ResumoMensalDTO> resumoMensal(
            @Valid @ModelAttribute ResumoMensalRequest request) {

        return new ApiResponse<>(200,
                lancamentoService.resumoMensal(
                        request.getMes(),
                        request.getAno()
                ));
    }

    @PostMapping
    public Lancamento criar(@RequestBody Lancamento lancamento) {
        return this.lancamentoService.salvar(lancamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        this.lancamentoService.deletar(id);

        return ResponseEntity.noContent().build();
    }


}
