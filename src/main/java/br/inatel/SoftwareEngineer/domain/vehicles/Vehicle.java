package br.inatel.SoftwareEngineer.domain.vehicles;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String plate;
    @Enumerated(EnumType.STRING)
    private VehicleType type;
    private Integer axles;
    private Boolean is_emergency;
    private LocalDateTime created_at;

    public Vehicle(String plate, VehicleType type, Integer axles, Boolean is_emergency) {
        this.plate = plate;
        this.type = type;
        this.axles = axles;
        this.is_emergency = is_emergency;
    }
}
