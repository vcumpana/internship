package com.endava.service_system.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Data
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Lob
    private byte[] content;
}
