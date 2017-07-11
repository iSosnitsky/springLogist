package sbat.logist.ru.transport.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "mat_view_big_select")
@NoArgsConstructor
@AllArgsConstructor
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "refreshMaterializedView", procedureName = "refreshMaterializedView")
})
public class MatViewBigSelect {
    @Id
    @Column(name = "REQUESTID")
    private Long requestId;
}
