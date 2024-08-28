package com.api.facturas.dao;

import com.api.facturas.pojo.Facturas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaDAO extends JpaRepository<Facturas,Integer> {

    List<Facturas> getFacturas();

    List<Facturas> getFacturasByUsername(@Param("username") String username);

}