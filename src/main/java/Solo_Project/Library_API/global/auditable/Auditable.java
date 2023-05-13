package Solo_Project.Library_API.global.auditable;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class Auditable {
    @CreatedDate
    @Column(name = "create_at", updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name="returned_at")
    private LocalDate returnedAt;
}
