package com.api.facturas.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NamedQuery(name = "Facturas.getFacturas",query = "select f from Facturas f order by f.id desc")
@NamedQuery(name = "Facturas.getFacturasByUsername",query = "select f from Facturas f where f.createdBy=:username order by f.id desc")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "facturas")

public class Facturas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "numeroContacto")
    private String numeroContacto;

    @Column(name = "metodoPago")
    private String metodoPago;

    @Column(name = "total")
    private Integer total;

    @Column(name = "productoDetalles",columnDefinition = "json")
    private String productoDetalles;

    @Column(name = "createdBy")
    private String createdBy;
}
