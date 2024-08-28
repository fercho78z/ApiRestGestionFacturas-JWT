package com.api.facturas.rest;

import com.api.facturas.constantes.FacturaConstantes;
import com.api.facturas.pojo.Categoria;
import com.api.facturas.service.CategoriaService;
import com.api.facturas.util.FacturaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/add")
    public ResponseEntity<String> agregarNuevaCategoria(@RequestBody(required = true)Map<String,String> requestMap){
        try{
            return categoriaService.addNuevaCategoria(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Categoria>> listarCategorias(@RequestParam(required = false) String valueFilter){
        try{
            return categoriaService.getAllCategorias(valueFilter);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResponseEntity<String> actualizarCategoria(@RequestBody(required = true) Map<String,String> requestMap){
        try{
            return categoriaService.updateCategoria(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
