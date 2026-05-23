package com.giuli.api_financeira.repositories;


import com.giuli.api_financeira.entities.Lancamento;
import com.giuli.api_financeira.entities.TipoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LancamentoRepository extends
        JpaRepository<Lancamento, Long> ,
        JpaSpecificationExecutor<Lancamento> {

 //    (Forma menos recomendada para filtros - usando consultas "PRONTAS"
    //    "Derived Query Method (consulta derivada pelo nome)"
//    List<Lancamento> findByDataBetween(LocalDate inicio, LocalDate fim);
//
//    List<Lancamento> findByDataBetweenAndTipo(LocalDate inicio, LocalDate fim, TipoLancamento tipo);

    // Forma mais recomendada para filtros - nenhum obrigatório, um metodo para todos
    @Query("""
        SELECT l FROM Lancamento l
        WHERE (CAST(:dataInicio AS date) IS NULL OR l.data >= :dataInicio)
        AND (CAST(:dataFim AS date) IS NULL OR l.data <= :dataFim)
        AND (:tipo IS NULL OR l.tipo = :tipo)
        AND (:valorMin IS NULL OR l.valor >= :valorMin)
        AND (:valorMax IS NULL OR l.valor <= :valorMax)
   """)
    Page<Lancamento> buscarComFiltros(
            @Param("dataInicio")
            LocalDate dataInicio,

            @Param("dataFim")
            LocalDate dataFim,

            @Param("tipo")
            TipoLancamento tipo,

            @Param("valorMin") BigDecimal valorMin,

            @Param("valorMax") BigDecimal valorMax,

            Pageable pageable);

    @Query("""
        SELECT COALESCE(SUM(l.valor),0)
        FROM Lancamento l
        WHERE l.empresa.id = :empresaId
        AND l.tipo = :tipo
        AND (:dataInicio IS NULL OR l.data >= :dataInicio)
        AND (:dataFim IS NULL OR l.data <= :dataFim)
    """)
    BigDecimal somarPorTipoEPeriodo(
            @Param("empresaId") Long empresaId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("tipo") TipoLancamento tipo
    );

    @Query("""
        SELECT
            EXTRACT(MONTH FROM l.data),
            SUM(CASE WHEN l.tipo = 'RECEITA' THEN l.valor ELSE 0 END),
            SUM(CASE WHEN l.tipo = 'DESPESA' THEN l.valor ELSE 0 END)
        FROM Lancamento l
        WHERE l.empresa.id = :empresaId
        AND EXTRACT(YEAR FROM l.data) = :ano
        GROUP BY EXTRACT(MONTH FROM l.data)
        ORDER BY EXTRACT(MONTH FROM l.data)
    """)
    List<Object[]> resumoPorAno(
            @Param("empresaId") Long empresaId,
            @Param("ano") int ano
    );

    @Query("""
        SELECT
            EXTRACT(MONTH FROM l.data),
            SUM(CASE WHEN l.tipo = 'RECEITA' THEN l.valor ELSE 0 END),
            SUM(CASE WHEN l.tipo = 'DESPESA' THEN l.valor ELSE 0 END)
        FROM Lancamento l
        WHERE l.empresa.id = :empresaId
        AND EXTRACT(YEAR FROM l.data) = :ano
        AND EXTRACT(MONTH FROM l.data) = :mes
        GROUP BY EXTRACT(MONTH FROM l.data)
    """)
    List<Object[]> resumoPorMes(
            @Param("empresaId") Long empresaId,
            @Param("ano") int ano,
            @Param("mes") int mes
    );

    // (consulta derivada pelo nome)
    Page<Lancamento> findByDescricaoContainingIgnoreCase (String descricao, Pageable pageable);

    List<Lancamento> findByEmpresaId(Long empresaId);

    Optional<Lancamento> findByIdAndEmpresaId(Long id, Long empresaId);

}
