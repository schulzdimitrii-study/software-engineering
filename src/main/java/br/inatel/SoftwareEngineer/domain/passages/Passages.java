package br.inatel.SoftwareEngineer.domain.passages;

import br.inatel.SoftwareEngineer.domain.vehicles.Vehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;


import br.inatel.SoftwareEngineer.domain.tallStation.TallStation;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "passages")
public class Passages {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "tall_station_id")
    private TallStation tallStation;

    private String location;
    private LocalDateTime timestamp;
    private BigDecimal amountPaid;
    private BigDecimal multiplierApplied;

    public Passages(Vehicle vehicle, String location, LocalDateTime timestamp, BigDecimal amountPaid,
            BigDecimal multiplierApplied) {
        this.vehicle = vehicle;
        this.location = location;
        this.timestamp = timestamp;
        this.amountPaid = amountPaid;
        this.multiplierApplied = multiplierApplied;
    }
}