package com.example.demo.controller;
import com.example.demo.entity.MyCall;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CustomerServiceControler {
	

	private LiveMonitor livemonitor;



	@RequestMapping(value = "/database", method=RequestMethod.GET)
	public String showForm(Model model) throws InvocationTargetException, IllegalAccessException {
		Filters filters= new Filters(new Date(),new Date(),new Date(),new Date(),"","","");
		model.addAttribute("filters", filters);
		List<MyCall> calls = LiveMonitor.getDateCalls(filters.applyAll());
		model.addAttribute("calls", calls);
  		return "database";
	}

	@RequestMapping(value = "/stats", method=RequestMethod.GET)
	public String showStats(Model model) throws InvocationTargetException, IllegalAccessException, IOException {
		Filters filters= new Filters(new Date(),new Date(),new Date(),new Date(),"","","");
		model.addAttribute("filters", filters);
		List<MyCall> calls = LiveMonitor.getDateCalls(filters.applyAll());
		StatsCounter stats = new StatsCounter(calls);
		model.addAttribute("stats", stats);
		return "stats";
	}

	@RequestMapping(value = "/stats", method=RequestMethod.POST, params="action=stats")
	public String showStats2(HttpServletResponse response,
							  @ModelAttribute(value="filters") Filters filters,
							  Model model) throws InvocationTargetException, IllegalAccessException, IOException {
		List<MyCall> calls = LiveMonitor.getDateCalls(filters.applyAll());
		StatsCounter stats = new StatsCounter(calls);
		model.addAttribute("stats", stats);
		return "stats";
	}
	@RequestMapping(value = "/database", method=RequestMethod.POST, params="action=Filter")
	public String processForm(HttpServletResponse response,
							  @ModelAttribute(value="filters") Filters filters,
							  Model model) throws InvocationTargetException, IllegalAccessException, IOException {
		List<MyCall> calls = LiveMonitor.getDateCalls(filters.applyAll());
		model.addAttribute("calls", calls);
		return "database";
	}

	@RequestMapping(value = "/database", method=RequestMethod.POST, params="action=excel")
	public String processFormExcel(HttpServletResponse response,
							  @ModelAttribute(value="filters") Filters filters,
							  Model model) throws InvocationTargetException, IllegalAccessException, IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=calls_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);
		List<MyCall> mycalls = LiveMonitor.getDateCalls(filters.applyAll());
		model.addAttribute("calls", mycalls);
		ExcelExporter excelExporter = new ExcelExporter(mycalls);
		excelExporter.export(response);

		return "database";
	}

	@GetMapping("/callsLive")
	public String listCalls(Model model) {
		model.addAttribute("calls", LiveMonitor.getDateCalls());
		List<String> g = new ArrayList<>();
		for (MyCall call : LiveMonitor.getDateCalls()){
			if (LiveMonitor.ourClientsTalking.containsKey(call.getClientNumber()) && !g.contains(call.getClientNumber())){
				if (call.getStatus().toString()=="completed" ){
					LiveMonitor.ourClientsTalking.remove(call.getClientNumber());
					LiveMonitor.ourClients.replace(call.getClientNumber(), "https://upload.wikimedia.org/wikipedia/commons/0/0e/Ski_trail_rating_symbol-green_circle.svg");
				}
			}
			if (call.getStatus().toString()=="in-progress"){
				g.add(call.getClientNumber());
				LiveMonitor.ourClientsTalking.putIfAbsent(call.getClientNumber(),call.getGreenwayNumber());
				LiveMonitor.ourClients.replace(call.getClientNumber(),"https://images.squarespace-cdn.com/content/v1/553f30aee4b0abb691b9c149/1557202379148-VJWBLQJC1W2NJJ58L2A1/Chatbot.gif");
			}
		}
		model.addAttribute("clients", LiveMonitor.getOurClients());
		model.addAttribute("clientkeys",LiveMonitor.getOurClients().keySet());
		return "calls";
	}





}
