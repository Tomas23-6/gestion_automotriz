package com.example.backend_automotriz.controller;

import com.example.backend_automotriz.model.Movimiento;
import com.example.backend_automotriz.service.InventarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoController {
    private final InventarioService service;

    public MovimientoController(InventarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Movimiento> getAll() {
        return service.listarMovimientos();
    }

    @PostMapping
    public Movimiento create(@RequestBody Movimiento movimiento) {
        return service.registrarMovimiento(movimiento);
    }
}