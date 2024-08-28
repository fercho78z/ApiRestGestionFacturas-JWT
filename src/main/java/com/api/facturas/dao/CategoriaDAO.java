package com.api.facturas.dao;

import com.api.facturas.pojo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CategoriaDAO extends JpaRepository<Categoria,Integer> {

    List<Categoria> getAllCategorias();	
/*
    ResponseEntity<List<Categoria>> getAllCategorias(String valueFilter);

    ResponseEntity<String> updateCategoria(Map<String,String> requestMap);*/
}