package br.feevale.joga_aurora.entity;

import br.feevale.joga_aurora.enums.RiskReferenceEnum;
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
@Table(name = "physical_test")
public class PhysicalTestEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(columnDefinition = "varchar(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, columnDefinition = "varchar(36)")
    private StudentEntity student;

    @Column(name = "collection_date", nullable = false, columnDefinition = "date")
    private Date collectionDate;

    @Column(name = "six_minutes_test", nullable = false, columnDefinition = "decimal(6,2)")
    private Double sixMinutesTest;

    @Convert(converter = RiskReferenceEnumConverter.class)
    @Column(name = "six_minutes_reference", nullable = false, columnDefinition = "char(1)")
    private RiskReferenceEnum sixMinutesReference;

    @Column(name = "flex_test", nullable = false, columnDefinition = "decimal(6,2)")
    private Double flexTest;

    @Convert(converter = RiskReferenceEnumConverter.class)
    @Column(name = "flex_reference", nullable = false, columnDefinition = "char(1)")
    private RiskReferenceEnum flexReference;

    @Column(name = "rml_test", nullable = false, columnDefinition = "decimal(6,2)")
    private Double rmlTest;

    @Convert(converter = RiskReferenceEnumConverter.class)
    @Column(name = "rml_reference", nullable = false, columnDefinition = "char(1)")
    private RiskReferenceEnum rmlReference;

    @Column(name = "twenty_meters_test", nullable = false, columnDefinition = "decimal(6,2)")
    private Double twentyMetersTest;

    @Convert(converter = RiskReferenceEnumConverter.class)
    @Column(name = "twenty_meters_reference", nullable = false, columnDefinition = "char(1)")
    private RiskReferenceEnum twentyMetersReference;

    @Column(name = "throw_two_kg_test", nullable = false, columnDefinition = "decimal(6,2)")
    private Double throwTwoKgTest;

    @Convert(converter = RiskReferenceEnumConverter.class)
    @Column(name = "throw_two_kg_reference", nullable = false, columnDefinition = "char(1)")
    private RiskReferenceEnum throwTwoKgReference;

}
