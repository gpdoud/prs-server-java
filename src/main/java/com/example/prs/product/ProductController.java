package com.example.prs.product;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductRepository productRepo;
	
	@GetMapping
	public ResponseEntity<List<Product>> GetAll() {
		var products = productRepo.findAll();
		return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}
	@GetMapping("{id}")
	public ResponseEntity<Product> Get(@PathVariable int id) {
		var product = productRepo.findById(id);
		if(product.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<Product> Create(@RequestBody Product product) {
		if(product == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var savedProduct = productRepo.save(product);
		return new ResponseEntity<Product>(savedProduct, HttpStatus.CREATED);
	}
	@PutMapping("{id}")
	public ResponseEntity<Product> Change(@PathVariable int id, @RequestBody Product product) {
		if(id != product.getId()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var savedProduct = productRepo.save(product);
		return new ResponseEntity<Product>(savedProduct, HttpStatus.NO_CONTENT);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<Product> Remove(@PathVariable int id) {
		Optional<Product> Product = productRepo.findById(id);
		if(Product.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		productRepo.deleteById(id);
		return new ResponseEntity<Product>(Product.get(), HttpStatus.NO_CONTENT);
	}
}
