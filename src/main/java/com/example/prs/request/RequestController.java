package com.example.prs.request;

import java.math.BigDecimal;
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
@RequestMapping("/api/requests")
public class RequestController {
	
	@Autowired
	private RequestRepository requestRepo;
	
	@GetMapping("reviews/{userId}")
	public ResponseEntity<List<Request>> GetAllInReview(@PathVariable int userId) {
		//var user = userRepo.findById(userId);
		var requests = requestRepo.findByStatusAndUserIdNot("REVIEW", userId);
		return new ResponseEntity<List<Request>>(requests, HttpStatus.OK);
	}
	@PutMapping("review/{id}")
	public ResponseEntity<Request> Review(@PathVariable int id, @RequestBody Request request) {
		var largestAutoApproveTotal = new BigDecimal(50);
		request.setStatus(request.getTotal().compareTo(largestAutoApproveTotal) <= 0 ? "APPROVED" : "REVIEW");
		return Change(id, request);
	}
	@PutMapping("approve/{id}")
	public ResponseEntity<Request> Approve(@PathVariable int id, @RequestBody Request request) {
		request.setStatus("APPROVED");
		request.setRejectionReason(null);
		return Change(id, request);
	}
	@PutMapping("reject/{id}")
	public ResponseEntity<Request> Reject(@PathVariable int id, @RequestBody Request request) {
		if(request.getRejectionReason() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		request.setStatus("REJECTED");
		return Change(id, request);
	}
	
	
	@GetMapping
	public ResponseEntity<List<Request>> GetAll() {
		var requests = requestRepo.findAll();
		return new ResponseEntity<List<Request>>(requests, HttpStatus.OK);
	}
	@GetMapping("{id}")
	public ResponseEntity<Request> Get(@PathVariable int id) {
		var request = requestRepo.findById(id);
		if(request.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Request>(request.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<Request> Create(@RequestBody Request request) {
		if(request == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var savedRequest = requestRepo.save(request);
		return new ResponseEntity<Request>(savedRequest, HttpStatus.CREATED);
	}
	@PutMapping("{id}")
	public ResponseEntity<Request> Change(@PathVariable int id, @RequestBody Request request) {
		if(id != request.getId()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var savedRequest = requestRepo.save(request);
		return new ResponseEntity<Request>(savedRequest, HttpStatus.NO_CONTENT);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<Request> Remove(@PathVariable int id) {
		Optional<Request> Request = requestRepo.findById(id);
		if(Request.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		requestRepo.deleteById(id);
		return new ResponseEntity<Request>(Request.get(), HttpStatus.NO_CONTENT);
	}


}
