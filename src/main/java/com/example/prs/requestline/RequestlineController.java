package com.example.prs.requestline;

import java.math.BigDecimal;
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

import com.example.prs.request.RequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/requestlines")
public class RequestlineController {

	@Autowired
	private RequestlineRepository requestlineRepo;
	@Autowired
	private RequestRepository requestRepo;
	
	private void RecalculateRequestTotal(int requestId) throws Exception {
		var request = requestRepo.findById(requestId);
		if(request.isEmpty()) {
			throw new Exception("RecalculateRequestTotal failed!");
		}
		var requestlines = requestlineRepo.findRequestlineByRequestId(requestId);
		var total = 0.0;
		for(var rl : requestlines) {
			total += rl.getQuantity() * rl.getProduct().getPrice();
		}
		request.get().setTotal(new BigDecimal(total));
		requestRepo.save(request.get());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Requestline> Get(@PathVariable int id) {
		var requestline = requestlineRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Requestline>(requestline.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<Requestline> Create(@RequestBody Requestline requestline) throws Exception {
		if(requestline == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var savedRequestline = requestlineRepo.save(requestline);
		RecalculateRequestTotal(requestline.getRequest().getId());
		return new ResponseEntity<Requestline>(savedRequestline, HttpStatus.CREATED);	
	}
	@PutMapping("{id}")
	public ResponseEntity<Requestline> Change(@PathVariable int id, @RequestBody Requestline requestline) throws Exception {
		if(id != requestline.getId()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var savedRequest = requestlineRepo.save(requestline);
		RecalculateRequestTotal(requestline.getRequest().getId());
		return new ResponseEntity<Requestline>(savedRequest, HttpStatus.NO_CONTENT);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<Requestline> Remove(@PathVariable int id) throws Exception {
		Optional<Requestline> requestline = requestlineRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		requestlineRepo.deleteById(id);
		RecalculateRequestTotal(requestline.get().getRequest().getId());
		return new ResponseEntity<Requestline>(requestline.get(), HttpStatus.NO_CONTENT);
	}
}
