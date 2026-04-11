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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

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
    @NotNull(message = "Vehicle is required")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "tall_station_id")
    @NotNull(message = "TallStation is required")
    private TallStation tallStation;

    @NotBlank(message = "Location is required")
    private String location;
    
    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;
    
    @NotNull(message = "Amount paid is required")
    @PositiveOrZero(message = "Amount paid must be positive or zero")
    private BigDecimal amountPaid;
    
    @PositiveOrZero(message = "Multiplier must be positive or zero")
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