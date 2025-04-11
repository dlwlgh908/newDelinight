package com.onetouch.delinight.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReplyDTO {
    private Long id;

    @NotBlank(message = "답변을 작성해주세요.")
    private String replyText;
    private String replyer;
    private QnaDTO qnaDTO;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    public ReplyDTO setQnaDTO(QnaDTO qnaDTO){
        this.qnaDTO =qnaDTO;
        return this;
    }

}
