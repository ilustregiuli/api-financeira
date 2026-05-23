package com.giuli.api_financeira.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SaldoDTO {

    private BigDecimal receitas;
    private BigDecimal despesas;
    private BigDecimal saldo;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public SaldoDTO(BigDecimal receitas, BigDecimal despesas, LocalDate dataInicio, LocalDate dataFim) {
        this.receitas = receitas;
        this.despesas = despesas;
        this.saldo = receitas.subtract(despesas);
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public BigDecimal getReceitas() {
        return receitas;
    }

    public BigDecimal getDespesas() {
        return despesas;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public LocalDate getDataInicio() {return dataInicio; }

    public LocalDate getDataFim() {return dataFim; }
}
