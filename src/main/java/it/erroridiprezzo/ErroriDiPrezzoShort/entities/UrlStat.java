package it.erroridiprezzo.ErroriDiPrezzoShort.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "URL_STATS")
public class UrlStat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "URL")
    private String url;

    @Column(name = "IP")
    private String ip;

    @Column(name = "USER_AGENT")
    private String userAgent;

    @Column(name = "TIME")
    @CreationTimestamp
    private LocalDateTime ts;

}
