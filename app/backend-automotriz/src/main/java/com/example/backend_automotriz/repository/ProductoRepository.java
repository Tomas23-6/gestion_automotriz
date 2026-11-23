package com.example.backend_automotriz.repository;

import com.example.backend_automotriz.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, String> {}