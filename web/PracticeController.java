package com.xlkj.project.modules.practice.web;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Optional;
import com.xlkj.framework.web.Servlets;
import com.xlkj.framework.web.base.BaseController;
import com.xlkj.project.domain.ProcessArea;
import com.xlkj.project.domain.Project;
import com.xlkj.project.domain.ProjectType;
import com.xlkj.project.domain.Result;
import com.xlkj.project.domain.SpePractices;
import com.xlkj.project.modules.practice.pojo.ModelDetail;
import com.xlkj.project.modules.practice.pojo.ModelEntry;
import com.xlkj.project.modules.practice.service.ProcticeService;

@Controller
@RequestMapping("/Practice")
public class PracticeController extends BaseController{

	private ProcticeService procticeService;
	
	@RequestMapping("")
	public String list(Model model,Pageable pageable,@RequestParam(value="search_PracticesName") String practicesName,Result result) {
		
		Page<ModelEntry> list=procticeService.findWithPage(pageable,practicesName);
		List<String> list2=procticeService.findTypes();
		
		model.addAttribute("modelTypes", list2);
		model.addAttribute("practice", list);
		
		setResult(model, result.isStatus(), result.getMessage());
		
		return "itpm/Practice/spePracticesList";
	}
	
	@RequestMapping("/active/{practiceModelKey}")
	public String active(Model model,Pageable pageable,@PathVariable String practiceModelKey,Result result) {
		
		Page<ModelDetail> list=procticeService.findDetaillWithPage(pageable, practiceModelKey);
		
		model.addAttribute("practiceDetail", list);
		
		setResult(model, result.isStatus(), result.getMessage());
		return "itpm/Practice/spePracticesDetailList";
	}
	
	//restful原则，动态指令交给http
	@RequestMapping(value="/",method=RequestMethod.PUT)
	public String modify(Model model,ModelEntry modelEntry,@RequestParam(value="dowhat") String dowhat,@RequestParam(value="practiceModelKey") String practiceModelKey,Result result) {
	
		if(dowhat == "into") {
			
			ModelEntry modelEntrys=procticeService.findModel("practiceModelKey");
			model.addAttribute("ModelEntry",modelEntry);
			return "itpm/Practice/spePracticesDetailModify";
		}else {
			
			procticeService.updataModel(modelEntry);
			
			setResult(model, result.isStatus(), result.getMessage());
			return "redirect:/Practice";
		}
		
	}
	
	@RequestMapping(value="/",method=RequestMethod.DELETE)
	public String delete(Model model,@RequestParam(value="practiceModelKey") String practiceModelKey,Result result) {
		
		procticeService.deleteModel(practiceModelKey);
		
		setResult(model, result.isStatus(), result.getMessage());
		
		return "redirect:/Practice";
	}
	
	public String importIn(Model model,@RequestParam(value="dowhat") String dowhat,@RequestParam(value="practiceModelKey") String practiceModelKey,ServletRequest request,Result result) {
		List<Project> Prolist=procticeService.listPro();
		List<ProjectType> Typelist=procticeService.listType();
		if(dowhat == "into") {		
			model.addAttribute("Prolist",Prolist);
			model.addAttribute("Typelist", Typelist);
			return "itpm/Practice/spePracticesDetailImprt";
			
		}else {
		Map<String, Object> option=Servlets.getParametersStartingWith(request, "_option");
		
		procticeService.importIn(option,practiceModelKey);
		return practiceModelKey;
		}
	}
}
