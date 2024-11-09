package org.yakdanol.task5_6.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_sequence")
    @SequenceGenerator(name = "location_sequence", sequenceName = "location_seq")
    private Long id;

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Event> events = new HashSet<>();
}
