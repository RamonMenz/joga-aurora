package br.feevale.joga_aurora.entity;

import br.feevale.joga_aurora.enums.converter.RiskReferenceEnumConverter;
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

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "body_measurement")
public class BodyMeasurementEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(columnDefinition = "varchar(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, columnDefinition = "varchar(36)")
    private StudentEntity student;

    @Column(name = "collection_date", nullable = false, columnDefinition = "date")
    private Date collectionDate;

    @Column(name = "waist", nullable = false, columnDefinition = "decimal(5,2)")
    private Double waist;

    @Column(name = "weight", nullable = false, columnDefinition = "decimal(5,2)")
    private Double weight;

    @Column(name = "height", nullable = false, columnDefinition = "integer")
    private Integer height;

    @Column(name = "bmi", nullable = false, columnDefinition = "decimal(5,2)")
    private Double bmi;

    @Convert(converter = RiskReferenceEnumConverter.class)
    @Column(name = "bmi_reference", nullable = false, columnDefinition = "char(1)")
    private String bmiReference;

    @Column(name = "waist_height_ratio", nullable = false, columnDefinition = "decimal(5,2)")
    private Double waistHeightRatio;

    @Convert(converter = RiskReferenceEnumConverter.class)
    @Column(name = "waist_height_reference", nullable = false, columnDefinition = "char(1)")
    private String waistHeightReference;

}
