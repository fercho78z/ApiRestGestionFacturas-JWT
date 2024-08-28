package com.api.facturas.pojo;

import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NamedQuery(name = "Producto.getAllProductos",query = "select new com.api.facturas.wrapper.ProductoWrapper(p.id,p.nombre,p.descripcion,p.precio,p.status,p.categoria.id,p.categoria.nombre) from Producto p")
@NamedQuery(name = "Producto.updateStatus",query = "update Producto p set p.status=:status where p.id=:id")
@NamedQuery(name = "Producto.getProductoByCategoria",query = "select new com.api.facturas.wrapper.ProductoWrapper(p.id,p.nombre) from Producto p where p.categoria.id=:id and p.status='true'")
@NamedQuery(name = "Producto.getProductoById",query = "select new com.api.facturas.wrapper.ProductoWrapper(p.id,p.nombre,p.descripcion,p.precio) from Producto p where p.id=:id")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_fk",nullable = false)
    private Categoria categoria;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Integer precio;

    @Column(name = "status")
    private String status;
}
