package com.api.facturas.service;
import com.api.facturas.dao.CategoriaDAO;
import com.api.facturas.dao.FacturaDAO;
import com.api.facturas.dao.ProductoDAO;
import com.api.facturas.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ProductoDAO productoDAO;

    @Autowired
    private CategoriaDAO categoriaDAO;

    @Autowired
    private FacturaDAO facturaDAO;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map = new HashMap<>();
        map.put("productos",productoDAO.count());
        map.put("categorias",categoriaDAO.count());
        map.put("facturas",facturaDAO.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
