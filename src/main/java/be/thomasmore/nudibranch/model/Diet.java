package be.thomasmore.nudibranch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

@Entity
public class Diet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dietId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_category_id") // explicitly set column name
    @NotNull(message = "Categorie is verplicht")
    private DietCategory dietCategory;

    private String dietScientificName;
    @NotBlank(message = "Gewone naam verplicht")
    private String dietCommonName;
    private String dietDescription;
    private String dietImageUrl;
    @ManyToMany(mappedBy = "diets")
    private Collection<Nudibranch> nudibranches;

    public Integer getDietId() {
        return dietId;
    }

    public void setDietId(Integer id) {
        this.dietId = id;
    }

    public DietCategory getDietCategory() {
        return dietCategory;
    }

    public void setDietCategory(DietCategory dietCategory) {
        this.dietCategory = dietCategory;
    }

    public String getDietScientificName() {
        return dietScientificName;
    }

    public void setDietScientificName(String dietScientificName) {
        this.dietScientificName = dietScientificName;
    }

    public String getDietCommonName() {
        return dietCommonName;
    }

    public void setDietCommonName(String dietCommonName) {
        this.dietCommonName = dietCommonName;
    }

    public String getDietDescription() {
        return dietDescription;
    }

    public void setDietDescription(String dietDescription) {
        this.dietDescription = dietDescription;
    }

    public String getDietImageUrl() {
        return dietImageUrl;
    }

    public void setDietImageUrl(String dietImageUrl) {
        this.dietImageUrl = dietImageUrl;
    }



    public Collection<Nudibranch> getNudibranches() {
        return nudibranches;
    }

    public void setNudibranches(Collection<Nudibranch> nudibranches) {
        this.nudibranches = nudibranches;
    }
}
