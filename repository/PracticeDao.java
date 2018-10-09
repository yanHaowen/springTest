package com.xlkj.project.modules.practice.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xlkj.project.domain.Project;
import com.xlkj.project.domain.ProjectType;
import com.xlkj.project.domain.SpePractices;
import com.xlkj.project.modules.practice.pojo.ModelDetail;
import com.xlkj.project.modules.practice.pojo.ModelEntry;

public interface PracticeDao {

	Page findWithPage(Pageable pageable, String practicesName);

	Page<ModelDetail> findDetaillWithPage(Pageable pageable, String practiceModelKey);

	List<String> findTypes();

	ModelEntry findModel(String practiceModelKey);

	void updataModel(ModelEntry spePractices);

	void deleteModel(String practiceModelKey);

	Project findPro();

	List<Project> listPro();

	List<ProjectType> listType();

	 void importIn(Map<String, Object> option, String practiceModelKey);



}
