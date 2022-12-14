package estoquedecerveja.dto;

import estoquedecerveja.enums.CervejaTipo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CervejaDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String nome;

    @NotNull
    @Size(min = 1, max = 200)
    private String marca;

    @NotNull
    @Max(500)
    private Integer max;

    @NotNull
    @Max(100)
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    @NotNull
    private CervejaTipo tipo;
}
