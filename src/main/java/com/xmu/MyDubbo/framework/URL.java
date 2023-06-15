package com.xmu.MyDubbo.framework;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


@Data
public class URL implements Serializable {

    private String hostname;
    private Integer port;
    private Integer weight;
    private Integer activeCount;
    public URL(String hostname, Integer port) {
        this.hostname = hostname;
        this.port = port;
        this.weight = 20;
        this.activeCount = 0;
    }

    public URL(String hostname, Integer port, Integer weight) {
        this.hostname = hostname;
        this.port = port;
        this.weight = weight;
        this.activeCount = 0;
    }

    public URL(String hostname, Integer port, Integer weight, Integer activeCount) {
        this.hostname = hostname;
        this.port = port;
        this.weight = weight;
        this.activeCount = activeCount;
    }
}
