package br.feevale.joga_aurora.entity;

import br.feevale.joga_aurora.enums.AttendanceStatusEnum;
import br.feevale.joga_aurora.enums.converter.AttendanceStatusEnumConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Entity
@Table(name = "attendance")
public class AttendanceEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(columnDefinition = "varchar(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, columnDefinition = "varchar(36)")
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false, columnDefinition = "varchar(36)")
    private LessonEntity lesson;

    @Convert(converter = AttendanceStatusEnumConverter.class)
    @Column(name = "status", nullable = false, columnDefinition = "char(1)")
    private AttendanceStatusEnum status;

}
