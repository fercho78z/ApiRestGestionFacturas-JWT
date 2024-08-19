package com.api.facturas.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//Este seria un DTO segun el video 52 de ApiRest
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWrapper {


    private Integer id;
    private String nombre;
    private String email;
    private String numeroDeContacto;
    private String status;

}
