package nl.siegmann.zoo.frontend.controller;

import lombok.extern.slf4j.Slf4j;
import nl.siegmann.zoo.frontend.service.PenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RequestMapping
@RestController
@Slf4j
public class ZooController {

	private final PenService penService;

	public ZooController(PenService penService) {
		this.penService = penService;
	}

	@RequestMapping(value = {"/api/zoo"})
	public Map<String, Object> getZooData() {
		return Collections.singletonMap("pens", penService.getAllPens());
	}
}
