package com.example.prs.vendor;

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
@RequestMapping(value="/api/vendors")
public class VendorController {

	@Autowired
	private VendorRepository vendorRepo;
	
	@GetMapping
	public ResponseEntity<List<Vendor>> GetAll() {
		var vendors = vendorRepo.findAll();
		return new ResponseEntity<List<Vendor>>(vendors, HttpStatus.OK);
	}
	@GetMapping("{id}")
	public ResponseEntity<Vendor> Get(@PathVariable int id) {
		var vendor = vendorRepo.findById(id);
		if(vendor.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vendor>(vendor.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<Vendor> Create(@RequestBody Vendor vendor) {
		if(vendor == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var savedVendor = vendorRepo.save(vendor);
		return new ResponseEntity<Vendor>(savedVendor, HttpStatus.CREATED);
	}
	@PutMapping("{id}")
	public ResponseEntity<Vendor> Change(@PathVariable int id, @RequestBody Vendor vendor) {
		if(id != vendor.getId()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var savedVendor = vendorRepo.save(vendor);
		return new ResponseEntity<Vendor>(savedVendor, HttpStatus.NO_CONTENT);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<Vendor> Remove(@PathVariable int id) {
		Optional<Vendor> Vendor = vendorRepo.findById(id);
		if(Vendor.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		vendorRepo.deleteById(id);
		return new ResponseEntity<Vendor>(Vendor.get(), HttpStatus.NO_CONTENT);
	}
}
