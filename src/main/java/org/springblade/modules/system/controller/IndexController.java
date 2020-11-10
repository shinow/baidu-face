package org.springblade.modules.system.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("/")
public class IndexController {

	@RequestMapping("/")
	public ModelAndView index() throws Exception {
		log.info("Welcome to Cross Manage System!");
		return new ModelAndView("index");
	}

}
