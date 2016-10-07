package at.tugraz.ist.RampupConfigurator;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		setDefaultSelections(model);
		return "home";
	}
	
	public Model setDefaultSelections( Model model) {
		
		model.addAttribute("configurator","SmartHome");
		
		return model;
	}
	
	// SHOW USER REQUIREMENTS and CONSTRAINTS for selected configurator
	@ResponseBody
	@RequestMapping(value = "/selectConfigurator", method = RequestMethod.POST)
	public ModelAndView selectConfigurator(HttpServletRequest request, Model model) {
		
		String configurator = request.getParameter("conf");
		
		String data = FileOperations.readFile(configurator, -1);
		
		ModelAndView mav = new ModelAndView("home");
		mav.addObject("fileData",data);
		mav.addObject("configurator",configurator);
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getConfiguration", method = RequestMethod.POST)
	public ModelAndView getConfiguration(HttpServletRequest request, Model model) {
		
		String results = "";
		
		String fileData = request.getParameter("fd");
		String configurator = request.getParameter("conf");
		
		boolean response = FileOperations.writeFile("runfile", fileData);
		
		ModelAndView mav = new ModelAndView("home");
		
		mav.addObject("fileData",fileData);
		mav.addObject("configurator",configurator);
	
		
		//String results ="<b>Recommended "+output+"s using "+algorithm+" Algorithm</b><br><br>";
		
        //results = Recommenders.getCBAppRecomm(profile, results);
		//String results = "Results will be printed here";
		
	    results += Potassco.callClingo();
			
		//ModelAndView mav = new ModelAndView("home");
		mav.addObject("results",results);
	
		return mav;
	}
	
}
