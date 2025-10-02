package br.feevale.joga_aurora.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "lesson")
public class LessonEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(columnDefinition = "varchar(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false, columnDefinition = "varchar(36)")
    private ClassroomEntity classroom;

    @Column(name = "lesson_date", nullable = false, columnDefinition = "date")
    private Date lessonDate;

}
