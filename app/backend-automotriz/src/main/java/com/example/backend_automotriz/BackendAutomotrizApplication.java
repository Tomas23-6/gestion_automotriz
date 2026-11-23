package com.example.backend_automotriz;

import com.example.backend_automotriz.model.Producto;
import com.example.backend_automotriz.model.Proveedor;
import com.example.backend_automotriz.repository.ProductoRepository;
import com.example.backend_automotriz.repository.ProveedorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendAutomotrizApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendAutomotrizApplication.class, args);
	}

	@Bean
	CommandLineRunner seedData(ProveedorRepository provRepo, ProductoRepository prodRepo) {
		return args -> {
			if (provRepo.count() == 0) {
				Proveedor p = new Proveedor();
				p.setProviderId("PROV-01");
				p.setName("Repuestos Santiago SpA");
				p.setContactName("Carlos Soto");
				p.setEmail("ventas@repuestossantiago.cl");
				p.setPhone("+56912345678");
				provRepo.save(p);
			}
			if (prodRepo.count() == 0) {
				Producto prod = new Producto();
				prod.setCode("ACE-10W40");
				prod.setName("Aceite Sintético 10W40");
				prod.setDescription("Bidón 4 litros");
				prod.setCategory("Lubricantes");
				prod.setBrand("Castrol");
				prod.setCurrentStock(12);
				prod.setMinStock(5);
				prod.setTargetStock(20);
				prod.setPurchasePrice(18500);
				prod.setSalePrice(26900);
				prod.setProviderId("PROV-01");
				prod.setLocationZone("A");
				prod.setLocationShelf("Estante 1");
				prod.setLocationPosition("Nivel 2");
				prodRepo.save(prod);
			}
		};
	}
}