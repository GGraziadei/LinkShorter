package it.erroridiprezzo.ErroriDiPrezzoShort.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "URLS")
public class Url {
    @Id
    @Column(name = "HASH"  )
    private String key;
    @Column(name = "URL" , nullable = false , unique = true    )
    private String url;
    @Column(name = "CLICK" )
    private Long click;
}
