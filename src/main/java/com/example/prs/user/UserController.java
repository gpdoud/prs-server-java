package com.example.prs.user;

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

@CrossOrigin()
@RestController
@RequestMapping(value="/api/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("login/{username}/{password}")
	public User Login(@PathVariable String username, @PathVariable String password) {
		return userRepo.findByUsernameAndPassword(username, password);
	}
	@PostMapping("login")
	public User Login(@RequestBody UserLoginView userLoginView) {
		return userRepo.findByUsernameAndPassword(userLoginView.username, userLoginView.password);
	}
	@GetMapping
	public ResponseEntity<List<User>> GetAll() {
		var users = userRepo.findAll();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	@GetMapping("{id}")
	public ResponseEntity<User> Get(@PathVariable int id) {
		Optional<User> user = userRepo.findById(id);
		if(user.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<User> Create(@RequestBody User user) {
		if(user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var savedUser = userRepo.save(user);
		return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
	}
	@PutMapping("{id}")
	public ResponseEntity<User> Change(@PathVariable int id, @RequestBody User user) {
		if(id != user.getId()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var savedUser = userRepo.save(user);
		return new ResponseEntity<User>(savedUser, HttpStatus.NO_CONTENT);
	}
	@DeleteMapping("{id}")
	public ResponseEntity<User> Remove(@PathVariable int id) {
		Optional<User> user = userRepo.findById(id);
		if(user.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		userRepo.deleteById(id);
		return new ResponseEntity<User>(user.get(), HttpStatus.NO_CONTENT);
	}
}
