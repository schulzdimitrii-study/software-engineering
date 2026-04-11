package br.inatel.SoftwareEngineer.domain.tallStation;

import br.inatel.SoftwareEngineer.domain.vehicles.Vehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tall_stations")
public class TallStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String location;

    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    public TallStation(String name, String location, BigDecimal basePrice) {
        this.name = name;
        this.location = location;
        this.basePrice = basePrice;
    }

    public BigDecimal calculateTall(Vehicle vehicle, LocalTime time) {

        if (Boolean.TRUE.equals(vehicle.getIs_emergency())) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        int axles = (vehicle.getAxles() != null && vehicle.getAxles() > 0) ? vehicle.getAxles() : 2;
        BigDecimal finalAmount = this.basePrice.multiply(BigDecimal.valueOf(axles));

        if (isPeakHour(time)) {
            finalAmount = finalAmount.multiply(new BigDecimal("1.20"));
        }

        return finalAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isPeakHour(LocalTime time) {
        /* Morning: 07:00 - 09:00 | Evening: 17:00 - 19:00 */

        if (time == null) {
            time = LocalTime.now();
        }
        int hour = time.getHour();
        boolean isMorningPeak = hour >= 7 && hour < 9;
        boolean isEveningPeak = hour >= 17 && hour < 19;

        return isMorningPeak || isEveningPeak;
    }
}
