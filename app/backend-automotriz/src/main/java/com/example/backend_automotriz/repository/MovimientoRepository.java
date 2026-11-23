package com.example.backend_automotriz.repository;

import com.example.backend_automotriz.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {}