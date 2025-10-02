package br.feevale.joga_aurora.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "classroom")
public class ClassroomEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(columnDefinition = "varchar(36)")
    private String id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(32)")
    private String name;

    @Column(name = "year", nullable = false, columnDefinition = "integer")
    private Integer year;

    @OneToMany(mappedBy = "classroom")
    private List<StudentEntity> studentList;

}
