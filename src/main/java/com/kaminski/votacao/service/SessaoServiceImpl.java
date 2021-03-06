package com.kaminski.votacao.service;

import com.kaminski.votacao.exception.RecursoNaoEncontradoException;
import com.kaminski.votacao.model.documents.Sessao;
import com.kaminski.votacao.model.dto.SessaoDto;
import com.kaminski.votacao.model.form.SessaoForm;
import com.kaminski.votacao.repository.SessaoRepository;
import com.kaminski.votacao.util.Validacao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SessaoServiceImpl implements SessaoService{

    private static final Integer TEMPO_DEFAULT = 1;
    private SessaoRepository sessaoRepository;
    private Validacao validacao;

    @Override
    public SessaoDto abrir(SessaoForm sessaoForm) {

        validacao.verificarSePautaExiste(sessaoForm.getPautaId());

        var sessao = Sessao.builder()
                .dataHoraInicio(LocalDateTime.now())
                .dataHoraFim(calcularTempoSessao(sessaoForm.getTempoDuracao()))
                .pautaId(sessaoForm.getPautaId())
                .status(Boolean.TRUE)
                .divulgada(Boolean.FALSE)
                .build();
        return SessaoDto.converterDocumentoParaDto(sessaoRepository.save(sessao));

    }

    @Override
    public Sessao encerrar(Sessao sessao) {
        sessao.setStatus(Boolean.FALSE);
        return sessaoRepository.save(sessao);
    }

    @Override
    public Sessao divulgar(Sessao sessao) {
        sessao.setDivulgada(Boolean.TRUE);
        return sessaoRepository.save(sessao);
    }

    @Override
    public List<Sessao> listarTodos() {
        return sessaoRepository.findAll();
    }

    @Override
    public List<Sessao> listarSessoesAbertas() {
        var sessoes = sessaoRepository.findByStatusTrue();
        return sessoes.stream()
                .filter(sessao -> sessao.getDataHoraFim().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Sessao> listarSessoesEncerradasNaoDivulgadas() {
        return sessaoRepository.findByStatusFalseAndDivulgadaFalse();
    }

    @Override
    public Sessao buscarPorId(String id) {
        validacao.verificarSeAssociadoExiste(id);
        return sessaoRepository.findById(id).get();
    }

    private LocalDateTime calcularTempoSessao(Integer minutos){
        if(minutos != null)
            return LocalDateTime.now().plusMinutes(minutos);
        return LocalDateTime.now().plusMinutes(TEMPO_DEFAULT);
    }

}
