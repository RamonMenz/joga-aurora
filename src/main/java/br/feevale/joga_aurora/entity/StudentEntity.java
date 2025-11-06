package br.feevale.joga_aurora.entity;

import br.feevale.joga_aurora.enums.GenderEnum;
import br.feevale.joga_aurora.enums.converter.GenderEnumConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "student")
public class StudentEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(columnDefinition = "varchar(36)")
    private String id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(32)")
    private String name;

    @Column(name = "birth_date", nullable = false, columnDefinition = "date")
    private Date birthDate;

    @Convert(converter = GenderEnumConverter.class)
    @Column(name = "gender", nullable = false, columnDefinition = "char(1)")
    private GenderEnum gender;

    @OneToMany(mappedBy = "student")
    @OrderBy("collectionDate DESC")
    private List<BodyMeasurementEntity> bodyMeasurementList;

    @OneToMany(mappedBy = "student")
    @OrderBy("collectionDate DESC")
    private List<PhysicalTestEntity> physicalTestList;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false, columnDefinition = "varchar(36)")
    private ClassroomEntity classroom;

}
