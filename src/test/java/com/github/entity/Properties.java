package github.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "PROPERTIES")
@Data
@Accessors(chain = true)
public class Properties implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "KEY")
    private String key;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "APPLICATION")
    private String application;

    @Column(name = "PROFILE")
    private String profile;

    @Column(name = "LABLE")
    private String lable;

    private static final long serialVersionUID = 1L;
}