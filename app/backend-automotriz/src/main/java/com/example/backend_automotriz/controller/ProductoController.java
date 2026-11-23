package com.example.backend_automotriz.controller;

import com.example.backend_automotriz.model.Producto;
import com.example.backend_automotriz.service.InventarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {
    private final InventarioService service;

    public ProductoController(InventarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Producto> getAll() {
        return service.listarProductos();
    }

    @PostMapping
    public Producto save(@RequestBody Producto producto) {
        return service.guardarProducto(producto);
    }

    @PutMapping("/{code}")
    public Producto update(@PathVariable String code, @RequestBody Producto producto) {
        producto.setCode(code);
        return service.guardarProducto(producto);
    }

    @DeleteMapping("/{code}")
    public void delete(@PathVariable String code) {
        service.eliminarProducto(code);
    }
}