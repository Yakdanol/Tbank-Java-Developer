package org.yakdanol.task5_6.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_sequence")
    @SequenceGenerator(name = "category_sequence", sequenceName = "category_seq")
    private Long id;

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false)
    private String name;
}
