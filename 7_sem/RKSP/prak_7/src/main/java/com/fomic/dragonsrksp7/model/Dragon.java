package com.fomic.dragonsrksp7.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("dragonsr")
public class Dragon {
    @Id
    private Long id;
    @Column("name")
    private String name;
    @Column("color")
    private String color;
    @Column("age")
    private Integer age;
    @Column("breed")
    private String breed;
}
