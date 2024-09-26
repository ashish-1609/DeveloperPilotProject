package com.pilot.project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
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
