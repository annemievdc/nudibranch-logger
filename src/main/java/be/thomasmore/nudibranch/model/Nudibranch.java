package be.thomasmore.nudibranch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Collection;

@Entity
public class Nudibranch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nudibranchId;

    @NotBlank(message = "Wetenschappelijke naam verplicht")
    private String scientificName;
    @NotBlank(message = "Gewone naam verplicht")
    private String commonName;

    private String imageUrl;
    private boolean venomous;
    private String description;
    @OneToMany(mappedBy = "nudibranch")
    private Collection<Sighting> sighting;



    @ManyToMany
    private Collection<Diet> diets;


    public Integer getNudibranchId() {
        return nudibranchId;
    }

    public void setNudibranchId(Integer nudibranchId) {
        this.nudibranchId = nudibranchId;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isVenomous() {
        return venomous;
    }

    public void setVenomous(boolean venomous) {
        this.venomous = venomous;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Sighting> getSighting() {
        return sighting;
    }

    public void setSighting(Collection<Sighting> sighting) {
        this.sighting = sighting;
    }
    public Collection<Diet> getDiets() {
        return diets;
    }

    public void setDiets(Collection<Diet> diets) {
        this.diets = diets;
    }

}
