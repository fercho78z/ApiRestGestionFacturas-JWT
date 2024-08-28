package com.api.facturas.service;



import com.api.facturas.constantes.FacturaConstantes;
import com.api.facturas.dao.CategoriaDAO;
import com.api.facturas.pojo.Categoria;
import com.api.facturas.jwt.JwtFilter;
import com.api.facturas.service.CategoriaService;
import com.api.facturas.util.FacturaUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaDAO categoriaDAO;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNuevaCategoria(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateCategoriaMap(requestMap,false)){
                    categoriaDAO.save(getCategoriaFromMap(requestMap,false));
                    return FacturaUtils.getResponseEntity("Categoría agregada con éxito",HttpStatus.OK);
                }
            }
            else{
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Categoria>> getAllCategorias(String valueFilter) {
        try{
            if(!Strings.isNullOrEmpty(valueFilter) && valueFilter.equalsIgnoreCase("true")){
                log.info("Usando el método getAllCategorias() de Categoria");
                return new ResponseEntity<List<Categoria>>(categoriaDAO.getAllCategorias(),HttpStatus.OK);
            }
            log.info("Usando el método findAll() de JpaRepository");
            return new ResponseEntity<List<Categoria>>(categoriaDAO.findAll(),HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<List<Categoria>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategoria(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateCategoriaMap(requestMap,true)){
                    Optional optional = categoriaDAO.findById(Integer.parseInt(requestMap.get("id")));
                    if(!optional.isEmpty()){
                        categoriaDAO.save(getCategoriaFromMap(requestMap,true));
                        return FacturaUtils.getResponseEntity("Categoría actualizada con éxito",HttpStatus.OK);
                    }
                    else{
                        return FacturaUtils.getResponseEntity("La categoría con ese ID no existe",HttpStatus.NOT_FOUND);
                    }
                }
            }
            else{
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoriaMap(Map<String,String> requestMap,boolean validateId){
        if(requestMap.containsKey("nombre")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }
            if(!validateId){
                return true;
            }
        }
        return false;
    }

    private Categoria getCategoriaFromMap(Map<String,String> requestMap,Boolean isAdd){
        Categoria categoria = new Categoria();
        if(isAdd){
            categoria.setId(Integer.parseInt(requestMap.get("id")));
        }
        categoria.setNombre(requestMap.get("nombre"));
        return categoria;
    }
}