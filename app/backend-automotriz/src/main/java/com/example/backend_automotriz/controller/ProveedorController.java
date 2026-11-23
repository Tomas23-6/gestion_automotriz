package com.example.backend_automotriz.controller;

import com.example.backend_automotriz.model.Proveedor;
import com.example.backend_automotriz.service.InventarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {
    private final InventarioService service;

    public ProveedorController(InventarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Proveedor> getAll() {
        return service.listarProveedores();
    }

    @PostMapping
    public Proveedor save(@RequestBody Proveedor proveedor) {
        return service.guardarProveedor(proveedor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.eliminarProveedor(id);
    }
}