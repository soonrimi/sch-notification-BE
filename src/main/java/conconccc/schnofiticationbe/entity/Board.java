package conconccc.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@DiscriminatorValue("BOARD")
@Schema(name = "Board", description = "건의사항 엔티티")
public class Board extends BasePost {
}
