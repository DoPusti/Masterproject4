package com.example.Masterproject4.CombinedRessources;

public class CombinedRessources {
    private Long id;
    private String gripperOrAxis;

    private Double price;

    public CombinedRessources(Long id, String gripperOrAxis, Double price) {
        this.id = id;
        this.gripperOrAxis = gripperOrAxis;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public String getGripperOrAxis() {
        return gripperOrAxis;
    }
}
