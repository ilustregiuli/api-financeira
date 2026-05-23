package com.giuli.api_financeira.specification;


import com.giuli.api_financeira.entities.Lancamento;
import com.giuli.api_financeira.entities.TipoLancamento;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LancamentoSpecification {

    public static Specification<Lancamento> descricaoContem(String descricao) {
        return (root, query, cb) -> {
            if (descricao == null || descricao.isEmpty()) return null;

            return cb.like(
                    cb.lower(root.get("descricao")),
                    "%" + descricao.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Lancamento> tipoIgual(TipoLancamento tipo) {
        return (root, query, cb) -> {
            if (tipo == null) return null;

            return cb.equal(root.get("tipo"), tipo);
        };
    }

    public static Specification<Lancamento> dataMaiorOuIgual(LocalDate dataInicio) {
        return (root, query, cb) -> {
            if (dataInicio == null) return null;

            return cb.greaterThanOrEqualTo(root.get("data"), dataInicio);
        };
    }

    public static Specification<Lancamento> dataMenorOuIgual(LocalDate dataFim) {
        return (root, query, cb) -> {
            if (dataFim == null) return null;

            return cb.lessThanOrEqualTo(root.get("data"), dataFim);
        };
    }

    public static Specification<Lancamento> valorMin(BigDecimal valorMin) {
        return (root, query, cb) -> {
            if (valorMin == null) return null;

            return cb.greaterThanOrEqualTo(root.get("valor"), valorMin);
        };
    }

    public static Specification<Lancamento> valorMax(BigDecimal valorMax) {
        return (root, query, cb) -> {
            if (valorMax == null) return null;

            return cb.lessThanOrEqualTo(root.get("valor"), valorMax);
        };
    }

    public static Specification<Lancamento> daEmpresa(Long empresaId) {
        return (root, query, cb) ->
                cb.equal(root.get("empresa").get("id"), empresaId);
    }
}
