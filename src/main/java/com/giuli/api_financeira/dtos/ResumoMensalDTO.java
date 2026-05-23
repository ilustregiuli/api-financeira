package com.giuli.api_financeira.dtos;

import java.math.BigDecimal;

public class ResumoMensalDTO {

    private Integer mes;
    private BigDecimal totalReceitas;
    private BigDecimal totalDespesas;

    public ResumoMensalDTO(Integer mes, BigDecimal totalReceitas, BigDecimal totalDespesas) {
        this.mes = mes;
        this.totalReceitas = totalReceitas;
        this.totalDespesas = totalDespesas;
    }

    public Integer getMes() {
        return mes;
    }

    public BigDecimal getReceitas() {
        return totalReceitas;
    }

    public BigDecimal getTotalDespesas() {
        return totalDespesas;
    }
}
