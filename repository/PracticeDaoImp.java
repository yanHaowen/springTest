package com.xlkj.project.modules.practice.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.StrBuilder;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.sun.org.apache.regexp.internal.recompile;
import com.xlkj.framework.persistence.jdbc.support.BaseDao;
import com.xlkj.project.domain.Project;
import com.xlkj.project.domain.ProjectType;
import com.xlkj.project.domain.SpePractices;
import com.xlkj.project.modules.bsif.areaManage.repository.AreaManageDaoImpl.AreaRowMapper;
import com.xlkj.project.modules.practice.pojo.ModelDetail;
import com.xlkj.project.modules.practice.pojo.ModelEntry;

import jdk.nashorn.internal.objects.annotations.Where;

import java.util.stream.IntStream;
@Repository
public class PracticeDaoImp extends BaseDao implements PracticeDao{

	@Override
	public Page<ModelEntry> findWithPage(Pageable pageable, String practicesName) {
		
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT   `projectRelID`,	pro.`projectName`,`practiceModelName`,`modelTypeName`,`model`.`modelTypeKey`,`practiceModelKey`,`projectRelID`,`ptype`.`projectName`"
				+ " FROM   `item_practicemodel` AS model JOIN `item_practicemodeltype` AS mtype JOIN `item_project` AS pro JOIN `item_projecttype` AS ptype")
		.append(" where `pro`.`projectTypeID` = `ptype`.`projectKey` and model.modelTypeKey = mtype.modelTypeKey");
		
		if(practicesName != null) {	
		sql.append("and model.`modelTypeKey` = ")
		.append(practicesName);
		}
		

		
		return queryForPage(sql.toString(), pageable,new ModelPractice(),null);
	}
	
	class ModelPractice implements ParameterizedRowMapper{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {		
			ModelEntry me=new ModelEntry();
			me.setPracticeModelName(rs.getString("practiceModelName"));
			me.setModelTypeKey(rs.getString("modelTypeKey"));
			me.setPracticeModelKey(rs.getString("practiceModelKey"));
			me.setmodelTypeName(rs.getString("modelTypeName"));
			me.setProjectType(rs.getString("projectName"));
			me.setProjectName(rs.getString("projectName"));
			
			return me;
		}	
	}

	@Override
	public Page<ModelDetail> findDetaillWithPage(Pageable pageable, String practiceModelKey) {
		StringBuffer sql=new StringBuffer();
		sql.append("select practiceModelName,processAreaName,spePracticeName from item_practicemodeldetail as detail join item_practicemodel as model"
					+ ", item_processarea as area, item_spepractice as spe where model.practiceModelKey = detail.practiceModelKey and "
					+ "area.processAreaKey = detail.processAreaKey and spePracticeKey = detail.spePracticeKey and practiceModelKey =")
		.append(practiceModelKey);
		
		
		return queryForPage(sql.toString(), pageable,new ModelDetailPractice(),null);
	}
	
	class ModelDetailPractice implements ParameterizedRowMapper{

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					
			ModelDetail modelDetail=new ModelDetail();
			modelDetail.setPracticeModelName(rs.getString("practiceModelName"));
			modelDetail.setProcessAreaName("processAreaName");
			modelDetail.setSpePracticeName("spePracticeName");
			
			
			modelDetail.setCheckerRole(rs.getString("checkerRole"));
			modelDetail.setExecuteSequence(rs.getInt("executeSequence"));
			/*与数据库交互次数过多  所以抛弃使用
			 * Map<String, String> CtoM=new HashMap<String, String>();      //因为要和大量表外联，所以用map描述关系
			CtoM.put("practiceModelKey", "item_practicemodel");
			CtoM.put("processAreaKey", "item_processarea");
			CtoM.put("specialPracticeKey", "item_spepractice");
			CtoM.put("item_practicemodel", "practiceModelName");
			CtoM.put("item_processarea", "processAreaName");
			CtoM.put("item_spepractice", "spePracticeName");
			String[] keys= {"practiceModelKey","processAreaKey","specialPracticeKey"};
			String[] maps= {"item_practicemodel", "item_processarea", "item_spepractice"};
			
			
			StringBuffer sql=new StringBuffer();
			Map<String, Object> result;
			for(int i=0;i<3;i++) {
				sql.append("select ")
				.append(CtoM.get(maps[i]))
				.append("from ")
				.append(maps[i])
				.append("where ")
				.append(keys[i])
				.append(" = ")
				.append(rs.getString(keys[i]));
				
				result=jdbcTemplate.queryForMap(sql.toString());
				StringBuffer methodName=new StringBuffer("get");
				methodName.append(keys[i]);
				Method method;
				try {
					method = modelDetail.getClass().getMethod(methodName.toString(), String.class);
					method.invoke(modelDetail, result.get(keys[i]));
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
*/
		return modelDetail;
		}
		
	}

	
	
	//选择框中的模板类型
	@Override
	public List<String> findTypes() {
		
		StringBuffer sql=new StringBuffer();
		sql.append("select `modelTypeKey` from item_practicemodeltype");
		
		List<String> list=jdbcTemplate.query(sql.toString(), new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {			
				return rs.getString("modelTypeKey");
			}
			
		});
		
		return list;

	}



	@Override
	public ModelEntry findModel(String practiceModelKey) {
		
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT   `projectRelID`,	pro.`projectName`,`practiceModelName`,`modelTypeName`,`model`.`modelTypeKey`,`practiceModelKey`,`projectRelID`,`ptype`.`projectName`"
				+ " FROM   `item_practicemodel` AS model JOIN `item_practicemodeltype` AS mtype JOIN `item_project` AS pro JOIN `item_projecttype` AS ptype")
		.append(" where `pro`.`projectTypeID` = `ptype`.`projectKey` and model.modelTypeKey = mtype.modelTypeKey")
		.append(" where model.`practiceModelKey` = ")
		.append(practiceModelKey);
		
		ModelEntry modelEntry=(ModelEntry) query(sql.toString(), new ModelPractice());
		
		return modelEntry;
	}



	@Override
	public void updataModel(ModelEntry modelEntry) {
		StringBuffer sql =new StringBuffer("update  `item_practicemodel` AS model JOIN `item_practicemodeltype` AS mtype JOIN `item_project` AS pro JOIN `item_projecttype` AS ptype "
				+ "set pro.`projectName` = ? , practiceModelName = ? , modelTypeName = ? ,`ptype`.`projectName` = ? where practiceModelKey = ")
				.append(modelEntry.getPracticeModelKey());
		
		
		update(sql.toString(), new String[] {modelEntry.getProjectName(),modelEntry.getPracticeModelName(),modelEntry.getmodelTypeName(),modelEntry.getProjectType()});
		
	}



	@Override
	public void deleteModel(String practiceModelKey) {
		
		StringBuffer sql=new StringBuffer("update `item_practicemodel` AS model JOIN `item_practicemodeltype` AS mtype JOIN `item_project` AS pro JOIN `item_projecttype` AS ptype")
				.append("set model.`deleteFlag` = 1 ,mtype.`deleteFlag` = 1 , pro.`deleteFlag` = 1 , ptype.`deleteFlag` = 1 where model.practiceModelKey = ?");
		
		update(sql.toString(), new String[] {practiceModelKey});
		
	}


	@Override
	public List<Project> listPro() {
		
		String sql="select `projectName`,projectID,projectTypeID  from item_project";
		
		List<Project> list=jdbcTemplate.query(sql.toString(),new ResultSetExtractor(){

			@Override
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				Project project=new Project();
				project.setProjectName(rs.getString("projectName"));
				project.setProjectID(rs.getString("projectID"));
				project.setProjectTypeID(rs.getString("projectTypeID"));
				return project;
			}
			
		});
		
		return list;
	}



	@Override
	public List<ProjectType> listType() {
		String sql="select ptype.`projectName`,projectKey  from item_project sa pro join item_projecttype as ptype where pro.projectTypeID = ptype.projectKey";
		
		List<ProjectType> list=jdbcTemplate.query(sql.toString(),new ResultSetExtractor(){

			@Override
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				ProjectType projectType=new ProjectType();
				projectType.setProjectName(rs.getString("projectName"));
				projectType.setProjectKey(rs.getString("projectKey"));
				return projectType;
			}
			
		});
		
		return list;
	}



	@Override
	public Project findPro() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void importIn(Map<String, Object> option, String practiceModelKey) {
		StringBuffer sql=new StringBuffer("select * from item_practicemodel  where practiceModelKey = ")
		.append(practiceModelKey);
		ModelEntry entry=jdbcTemplate.query(sql.toString(), new ResultSetExtractor() {

			@Override
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				ModelEntry entry=new ModelEntry();
				entry.setmodelTypeName(rs.getString("modelTypeKey"));
				entry.setCreaterKey(rs.getString("createrKey"));
				entry.setCreateUser(rs.getString("createUser"));
				entry.setInstructions(rs.getString("instructions"));
				entry.setModelTypeKey(rs.getString("modelTypeKey"));
				entry.setPracticeModelID(rs.getString("practiceModelID"));
				return entry;
			}	
		});
		
		entry.setProjectRelKey(option.get("projectRelKey").toString());
		
		String Insertsql="insert into item_practicemodel(`practiceModelKey`,modelTypeKey , createrKey, createUser, instructions" + 
				"modelTypeKey, practiceModelID, projectRelKey) values(?,?,?,?,?,?,?,?) ";
		String random="daidhqf8y813fh31802h13ifbqwfnqif";
		String[] args=new String[] {random,entry.getModelTypeKey(),entry.getCreaterKey(),entry.getCreateUser(),
				entry.getCreateUser(),entry.getInstructions(),entry.getModelTypeKey(),entry.getPracticeModelID(),entry.getProjectRelKey()};
		
		jdbcTemplate.update(sql.toString(), args);
		
		
		//第二步  将detail数据插入
		StringBuffer sql2=new StringBuffer("select * from item_practicemodeldetail  where practiceModelKey = ")
		.append(practiceModelKey);
		
		query(sql2.toString(), new detailRowMapper(random));
		
	
	
		
		
	}


	class detailRowMapper implements ParameterizedRowMapper{
		String practiceModelKey;
		public detailRowMapper(String practiceModelKey) {
			this.practiceModelKey=practiceModelKey;
		}
		
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			String sqlInsert="insert into item_practicemodeldetail(practiceModelKey,modelDetailKey,processAreaKey,specialPracticeKey,executerRole,checkerRole,executeSequence,commont,createrKey,createDate,modifierKey,modifyDate,version,deleteFlag)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			String[] args= {practiceModelKey,rs.getString("modelDetailKey"),rs.getString("processAreaKey"),rs.getString("specialPracticeKey"),rs.getString("executerRole"),rs.getString("checkerRole"),rs.getString("executeSequence"),rs.getString("commont"),rs.getString("createrKey"),
					rs.getString("createDate"),rs.getString("modifierKey"),rs.getString("modifyDate"),rs.getString("version"),rs.getString("deleteFlag")};
			jdbcTemplate.update(sqlInsert, args);
			return null;
		}
		
	}
	
	
	

}
