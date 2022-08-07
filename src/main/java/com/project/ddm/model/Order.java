package com.project.ddm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "orders")
@JsonDeserialize(builder = Order.Builder.class)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long trackId;

    private Double weight;

    private Double price;

    private String sendingAddress;

    private String receivingAddress;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_name")
    private User user;

    @OneToOne (cascade = CascadeType.ALL)
    //@JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;

    public Order() {}

    private Order(Builder builder) {
        this.id = builder.id;
        this.trackId = builder.trackId;
        this.weight = builder.weight;
        this.price = builder.price;
        this.sendingAddress = builder.sendingAddress;
        this.receivingAddress = builder.receivingAddress;
        this.user = builder.user;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
//    public Device getDevice(){
//        return device;
//    }

    public Long getOrderId() {
        return id;
    }

    public Long getTrackId() {
        return trackId;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getPrice() {
        return price;
    }

    public String getSendingAddress() {
        return sendingAddress;
    }

    public String getReceivingAddress() {
        return receivingAddress;
    }

    public static class Builder {

        @JsonProperty("id")
        private Long id;

        @JsonProperty("track_id")
        private Long trackId;

        @JsonProperty("weight")
        private Double weight;

        @JsonProperty("price")
        private Double price;

        @JsonProperty("sending_address")
        private String sendingAddress;

        @JsonProperty("receiving_address")
        private String receivingAddress;

        @JsonProperty("user")
        private User user;


        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setTrackId(Long trackId) {
            this.trackId = trackId;
            return this;
        }

        public Builder setWeight(Double weight) {
            this.weight = weight;
            return this;
        }

        public Builder setPrice(Double price) {
            this.price = price;
            return this;
        }

        public Builder setSendingAddress(String sendingAddress) {
            this.sendingAddress = sendingAddress;
            return this;
        }

        public Builder setReceivingAddress(String receivingAddress) {
            this.receivingAddress = receivingAddress;
            return this;
        }

        public Builder setUser(User user){
            this.user = user;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}