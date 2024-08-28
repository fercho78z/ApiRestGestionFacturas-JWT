package com.api.facturas.service;

import com.api.facturas.pojo.Facturas;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface FacturaService{

    ResponseEntity<String> generateReport(Map<String,Object> requestMap);

    ResponseEntity<List<Facturas>> getFacturas();

    ResponseEntity<byte[]> getPdf(Map<String,Object> requestMap);

    ResponseEntity<String> deleteFactura(Integer id);
}
