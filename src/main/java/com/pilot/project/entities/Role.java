package com.pilot.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    private int id;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
 enum RoleType{
    ROLE_ADMIN,
    ROLE_USER
}
