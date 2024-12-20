package kr.co.porkandspoon.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import kr.co.porkandspoon.service.BoardService;

@RestController
public class BoardController {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired BoardService boardService;
	
	@GetMapping(value="/board/View")
	public ModelAndView boardView(Model model) {
		model.addAttribute("test", "hello");
		return new ModelAndView("/board/boardList");
	}
	
	@GetMapping(value="/boardmy/View")
	public ModelAndView boardmyView(Model model) {
		model.addAttribute("test", "hellow");
		return new ModelAndView("/board/boardList");
	}
	
	@GetMapping(value="/boardwrite/View")
	public ModelAndView boardwriteView() {
		return new ModelAndView("/board/boardWrite");
	}
	
	@GetMapping(value="/boardupdate/View")
	public ModelAndView boardupdateView() {
		return new ModelAndView("/board/boardUpdate");
	}
	
	@GetMapping(value="/boarddetail/View")
	public ModelAndView boarddetailView() {
		return new ModelAndView("/board/boardDetail");
	}
}
