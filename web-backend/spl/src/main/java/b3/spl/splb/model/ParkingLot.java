package b3.spl.splb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private float price;
    private boolean approved;
    @ManyToOne
    @JsonIgnore
    private AppUser user;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Collection<ParkingSpot> spots = new ArrayList<>();
}
