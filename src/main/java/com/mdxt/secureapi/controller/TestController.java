package com.mdxt.secureapi.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdxt.secureapi.entity.TestClass;
import com.mdxt.secureapi.security.RolesEnum;

@RestController
@RequestMapping("api/v1/tests")
public class TestController {

	private static final List<TestClass> students = new ArrayList<>();
	static {
		students.add(new TestClass(1, "James Bond"));
		students.add(new TestClass(2, "Bruce Wayne"));
	}
	
	@GetMapping(path = "test0/{id}")
	public TestClass getData(@PathVariable("id") Integer id) {
		return students.stream()
				.filter(testClass -> testClass.getId()==id)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No TestClass of id "+id));
	}
	
	@PostMapping(path = "test0")
	public void postData(@RequestBody TestClass testClass) {
		System.out.println("posted data - "+testClass.getData());
		students.add(testClass);
	}
	
	@GetMapping(path = "test0")
	public List<TestClass> getAllData() {
		return students;
	}
	
	@GetMapping("public/test1")
	public ResponseEntity<String> getPublicData() {
		return new ResponseEntity<String>("some public data", HttpStatus.ACCEPTED);
	}
	
	@GetMapping("admin/test2")
	public ResponseEntity<String> getAdminOnlyData() {
		return new ResponseEntity<String>("some admin only data", HttpStatus.ACCEPTED);
	}
}
