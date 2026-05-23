package com.giuli.api_financeira.services;


import com.giuli.api_financeira.dtos.LancamentoDTO;
import com.giuli.api_financeira.dtos.ResumoMensalDTO;
import com.giuli.api_financeira.dtos.SaldoDTO;
import com.giuli.api_financeira.dtos.reponses.PageResponse;
import com.giuli.api_financeira.entities.Empresa;
import com.giuli.api_financeira.entities.Lancamento;
import com.giuli.api_financeira.entities.TipoLancamento;
import com.giuli.api_financeira.repositories.LancamentoRepository;
import com.giuli.api_financeira.security.UserContext;
import com.giuli.api_financeira.specification.LancamentoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;



@Service
public class LancamentoService {

    private final LancamentoRepository repository;

    public LancamentoService(LancamentoRepository repository) {
        this.repository = repository;
    }

    public Lancamento salvar(Lancamento lancamento) {

        Long empresaId = UserContext.getEmpresaId();

        if (empresaId == null) {
            throw new RuntimeException("Empresa não identificada no contexto");
        }

        // evita buscar no banco (performance)
        Empresa empresa = new Empresa();
        empresa.setId(empresaId);

        lancamento.setEmpresa(empresa);

        return this.repository.save(lancamento);
    }

    public List<Lancamento> listar() {
        Long empresaId = UserContext.getEmpresaId();
        return this.repository.findByEmpresaId(empresaId);
    }

    public Lancamento buscarPorId(Long id) {
        Long empresaId = UserContext.getEmpresaId();

        return repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new RuntimeException("Lançamento não encontrado."));
    }

    public void deletar(Long id) {
        Long empresaId = UserContext.getEmpresaId();

        Lancamento lancamento = repository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new RuntimeException("Lançamento não encontrado."));

        repository.delete(lancamento);
    }

    public PageResponse<LancamentoDTO> buscarComFiltros(
            String descricao,
            LocalDate dataInicio,
            LocalDate dataFim,
            TipoLancamento tipo,
            BigDecimal valorMin,
            BigDecimal valorMax,
            Pageable pageable) {

        Long empresaId = UserContext.getEmpresaId();

        Specification<Lancamento> spec = Specification.allOf();

        spec = spec.and(LancamentoSpecification.daEmpresa(empresaId));
        spec = spec.and(LancamentoSpecification.descricaoContem(descricao));
        spec = spec.and(LancamentoSpecification.tipoIgual(tipo));
        spec = spec.and(LancamentoSpecification.dataMaiorOuIgual(dataInicio));
        spec = spec.and(LancamentoSpecification.dataMenorOuIgual(dataFim));
        spec = spec.and(LancamentoSpecification.valorMin(valorMin));
        spec = spec.and(LancamentoSpecification.valorMax(valorMax));

        Page<Lancamento> pagina = repository.findAll(spec, pageable);

        Page<LancamentoDTO> paginaDTO = pagina.map(this::converter);

        return new PageResponse<>(
                paginaDTO.getContent(),
                paginaDTO.getNumber(),
                paginaDTO.getSize(),
                paginaDTO.getTotalElements(),
                paginaDTO.getTotalPages()
        );
    }

    public LancamentoDTO converter(Lancamento l) {

        LancamentoDTO dto = new LancamentoDTO();

        dto.setId(l.getId());
        dto.setData(l.getData());
        dto.setDescricao(l.getDescricao());
        dto.setValor(l.getValor());
        dto.setTipo(l.getTipo());

        return dto;
    }

    public SaldoDTO calcularSaldo(LocalDate dataInicio, LocalDate dataFim) {

        Long empresaId = UserContext.getEmpresaId();

        BigDecimal receitas = repository.somarPorTipoEPeriodo(
                empresaId,
                dataInicio,
                dataFim,
                TipoLancamento.RECEITA
        );

        BigDecimal despesas = repository.somarPorTipoEPeriodo(
                empresaId,
                dataInicio,
                dataFim,
                TipoLancamento.DESPESA
        );

        return new SaldoDTO(receitas, despesas, dataInicio, dataFim);
    }

    public List<ResumoMensalDTO> resumoAnual(Integer ano) {

        Long empresaId = UserContext.getEmpresaId();

        int anoConsulta = (ano == null)
                ? LocalDate.now().getYear()
                : ano;

        List<Object[]> dados =
                repository.resumoPorAno(empresaId, anoConsulta);

        return dados.stream()
                .map(obj -> new ResumoMensalDTO(
                        ((Number) obj[0]).intValue(),
                        (BigDecimal) obj[1],
                        (BigDecimal) obj[2]
                ))
                .toList();
    }

    public ResumoMensalDTO resumoMensal(Integer mes, Integer ano) {

        Long empresaId = UserContext.getEmpresaId();

        int anoConsulta = (ano == null)
                ? LocalDate.now().getYear()
                : ano;

        List<Object[]> dados =
                repository.resumoPorMes(empresaId, anoConsulta, mes);

        if (dados.isEmpty()) {
            return new ResumoMensalDTO(
                    mes,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
        }

        Object[] obj = dados.get(0);

        return new ResumoMensalDTO(
                ((Number) obj[0]).intValue(),
                (BigDecimal) obj[1],
                (BigDecimal) obj[2]
        );
    }
}
