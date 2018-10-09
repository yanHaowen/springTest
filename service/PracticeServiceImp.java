package com.xlkj.project.modules.practice.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.xlkj.project.domain.Project;
import com.xlkj.project.domain.ProjectType;
import com.xlkj.project.domain.SpePractices;
import com.xlkj.project.modules.practice.pojo.ModelDetail;
import com.xlkj.project.modules.practice.pojo.ModelEntry;
import com.xlkj.project.modules.practice.repository.PracticeDao;

@Service
public class PracticeServiceImp implements ProcticeService{

	private PracticeDao practiceDao;
	
	@Override
	public Page findWithPage(Pageable pageable, String practicesName) {

		return practiceDao.findWithPage(pageable, practicesName);
	}

	@Override
	public Page<ModelDetail> findDetaillWithPage(Pageable pageable, String practiceModelKey) {
		
		return practiceDao.findDetaillWithPage(pageable,practiceModelKey);
	}

	@Override
	public List<String> findTypes() {
		
		return practiceDao.findTypes();
	}

	@Override
	public ModelEntry findModel(String string) {
		
		return practiceDao.findModel(string);
	}

	@Override
	public void updataModel(ModelEntry spePractices) {
		
		practiceDao.updataModel(spePractices);
		
	}

	@Override
	public void deleteModel(String practiceModelKey) {
		practiceDao.deleteModel(practiceModelKey);
		
	}

	@Override
	public Project findPro() {
		return null;
		
		
	}

	@Override
	public List<Project> listPro() {
		
		return practiceDao.listPro();
	}

	@Override
	public List<ProjectType> listType() {
		
		return practiceDao.listType();
	}

	@Override
	public  void importIn(Map<String, Object> option, String practiceModelKey) {
		
		
		practiceDao.importIn(option,practiceModelKey);
	}

}
