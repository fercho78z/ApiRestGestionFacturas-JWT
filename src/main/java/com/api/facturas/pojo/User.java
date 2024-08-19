package com.api.facturas.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "User.findByEmail",query = "select u from User u where u.email=:email")   //User es el nombre de la entidad-clase no de la tabla por eso falla si quiero ponerle el mismo nombre de la tabla que es user en plural y en minusculas
@NamedQuery(name = "User.getAllUsers",query = "select new com.api.facturas.wrapper.UserWrapper(u.id,u.nombre,u.email,u.numeroDeContacto,u.status) from User u where u.role='user'")
@NamedQuery(name = "User.updateStatus",query = "update User u set u.status=:status where u.id=:id")
//@NamedQuery(name = "User.getAllAdmins",query = "select u.email from User u where u.role='user'") 
@NamedQuery(name = "User.getAllAdmins",query = "select u.email from User u where u.role='admin'") 

@Data
@Entity
@DynamicUpdate  //Estos metodos se usan para solo actualizar o insertar datos digamos en dos columnas de 100 columnas
@DynamicInsert  //
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "numeroDeContacto")  //Lo raro es que ne la base de datos se crea la colimna como numero_de_contacto
    private String numeroDeContacto;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

}
