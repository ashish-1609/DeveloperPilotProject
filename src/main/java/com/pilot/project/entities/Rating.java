package com.pilot.project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    private String id;
    private int points;
    private String comment;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private Hotel hotel;
}
