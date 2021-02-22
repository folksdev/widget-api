package com.miro.widgetapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "widget")
public class Widget implements Serializable {

    private static final long serialVersionUID = -2684875470644906123L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private Integer x;
    private Integer y;

    @Column(unique = true)
    private Integer z;

    private Integer width;
    private Integer height;
    private Instant lastModificationDate;

    @Version
    private Long version;
}
