package com.kaminski.votacao.model.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class SessaoForm {

    @NotEmpty
    private String pautaId;

    @Positive
    private Integer tempoDuracao;

}
