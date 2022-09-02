package it.erroridiprezzo.ErroriDiPrezzoShort.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.dialect.MySQL5Dialect;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "PAGES")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title" )
    private String title;

    @Column(name = "path" )
    private String bodyPath;

    @Column(name = "ts_upload")
    @CreationTimestamp
    private LocalDateTime tsUpload;

    @Column(name = "enable")
    private boolean enable;

}
