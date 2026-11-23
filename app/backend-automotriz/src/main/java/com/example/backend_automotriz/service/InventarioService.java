package com.example.backend_automotriz.service;

import com.example.backend_automotriz.model.Movimiento;
import com.example.backend_automotriz.model.Producto;
import com.example.backend_automotriz.model.Proveedor;
import com.example.backend_automotriz.repository.MovimientoRepository;
import com.example.backend_automotriz.repository.ProductoRepository;
import com.example.backend_automotriz.repository.ProveedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InventarioService {
    private final ProductoRepository productoRepo;
    private final MovimientoRepository movimientoRepo;
    private final ProveedorRepository proveedorRepo;

    public InventarioService(
            ProductoRepository productoRepo,
            MovimientoRepository movimientoRepo,
            ProveedorRepository proveedorRepo
    ) {
        this.productoRepo = productoRepo;
        this.movimientoRepo = movimientoRepo;
        this.proveedorRepo = proveedorRepo;
    }

    public List<Producto> listarProductos() {
        return productoRepo.findAll();
    }

    public Producto guardarProducto(Producto p) {
        p.setLastUpdate(System.currentTimeMillis());
        return productoRepo.save(p);
    }

    public void eliminarProducto(String code) {
        if (productoRepo.existsById(code)) {
            productoRepo.deleteById(code);
        }
    }

    public List<Movimiento> listarMovimientos() {
        return movimientoRepo.findAll();
    }

    @Transactional
    public Movimiento registrarMovimiento(Movimiento mov) {
        Producto prod = productoRepo.findById(mov.getProductCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        boolean esSalida = List.of("VENTA", "CONSUMO", "DEVOLUCION").contains(mov.getMovementType());
        if (esSalida && prod.getCurrentStock() < mov.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock insuficiente");
        }

        int stockAnterior = prod.getCurrentStock();
        int stockResultante = esSalida ? stockAnterior - mov.getQuantity() : stockAnterior + mov.getQuantity();

        prod.setCurrentStock(stockResultante);
        prod.setLastUpdate(System.currentTimeMillis());
        productoRepo.save(prod);

        mov.setId(null);
        mov.setPreviousStock(stockAnterior);
        mov.setResultingStock(stockResultante);
        mov.setTimestamp(System.currentTimeMillis());
        return movimientoRepo.save(mov);
    }

    public List<Proveedor> listarProveedores() {
        return proveedorRepo.findAll();
    }

    public Proveedor guardarProveedor(Proveedor p) {
        return proveedorRepo.save(p);
    }

    public void eliminarProveedor(String id) {
        if (proveedorRepo.existsById(id)) {
            proveedorRepo.deleteById(id);
        }
    }
}