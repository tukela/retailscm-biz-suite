
package com.doublechaintech.retailscm.originalvouchercreation;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import com.doublechaintech.retailscm.RetailscmNamingServiceDAO;
import com.doublechaintech.retailscm.BaseEntity;
import com.doublechaintech.retailscm.SmartList;
import com.doublechaintech.retailscm.AccessKey;
import com.doublechaintech.retailscm.DateKey;
import com.doublechaintech.retailscm.StatsInfo;
import com.doublechaintech.retailscm.StatsItem;

import com.doublechaintech.retailscm.MultipleAccessKey;
import com.doublechaintech.retailscm.RetailscmUserContext;


import com.doublechaintech.retailscm.originalvoucher.OriginalVoucher;

import com.doublechaintech.retailscm.originalvoucher.OriginalVoucherDAO;



import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;


public class OriginalVoucherCreationJDBCTemplateDAO extends RetailscmNamingServiceDAO implements OriginalVoucherCreationDAO{


			
		
	
  	private  OriginalVoucherDAO  originalVoucherDAO;
 	public void setOriginalVoucherDAO(OriginalVoucherDAO pOriginalVoucherDAO){
 	
 		if(pOriginalVoucherDAO == null){
 			throw new IllegalStateException("Do not try to set originalVoucherDAO to null.");
 		}
	 	this.originalVoucherDAO = pOriginalVoucherDAO;
 	}
 	public OriginalVoucherDAO getOriginalVoucherDAO(){
 		if(this.originalVoucherDAO == null){
 			throw new IllegalStateException("The originalVoucherDAO is not configured yet, please config it some where.");
 		}
 		
	 	return this.originalVoucherDAO;
 	}	
 	
			
		

	
	/*
	protected OriginalVoucherCreation load(AccessKey accessKey,Map<String,Object> options) throws Exception{
		return loadInternalOriginalVoucherCreation(accessKey, options);
	}
	*/
	
	protected String getIdFormat()
	{
		return getShortName(this.getName())+"%06d";
	}
	
	public OriginalVoucherCreation load(String id,Map<String,Object> options) throws Exception{
		return loadInternalOriginalVoucherCreation(OriginalVoucherCreationTable.withId(id), options);
	}
	
	
	
	public OriginalVoucherCreation save(OriginalVoucherCreation originalVoucherCreation,Map<String,Object> options){
		
		String methodName="save(OriginalVoucherCreation originalVoucherCreation,Map<String,Object> options)";
		
		assertMethodArgumentNotNull(originalVoucherCreation, methodName, "originalVoucherCreation");
		assertMethodArgumentNotNull(options, methodName, "options");
		
		return saveInternalOriginalVoucherCreation(originalVoucherCreation,options);
	}
	public OriginalVoucherCreation clone(String originalVoucherCreationId, Map<String,Object> options) throws Exception{
	
		return clone(OriginalVoucherCreationTable.withId(originalVoucherCreationId),options);
	}
	
	protected OriginalVoucherCreation clone(AccessKey accessKey, Map<String,Object> options) throws Exception{
	
		String methodName="clone(String originalVoucherCreationId,Map<String,Object> options)";
		
		assertMethodArgumentNotNull(accessKey, methodName, "accessKey");
		assertMethodArgumentNotNull(options, methodName, "options");
		
		OriginalVoucherCreation newOriginalVoucherCreation = loadInternalOriginalVoucherCreation(accessKey, options);
		newOriginalVoucherCreation.setVersion(0);
		
		
 		
 		if(isSaveOriginalVoucherListEnabled(options)){
 			for(OriginalVoucher item: newOriginalVoucherCreation.getOriginalVoucherList()){
 				item.setVersion(0);
 			}
 		}
		

		
		saveInternalOriginalVoucherCreation(newOriginalVoucherCreation,options);
		
		return newOriginalVoucherCreation;
	}
	
	
	
	

	protected void throwIfHasException(String originalVoucherCreationId,int version,int count) throws Exception{
		if (count == 1) {
			throw new OriginalVoucherCreationVersionChangedException(
					"The object version has been changed, please reload to delete");
		}
		if (count < 1) {
			throw new OriginalVoucherCreationNotFoundException(
					"The " + this.getTableName() + "(" + originalVoucherCreationId + ") has already been deleted.");
		}
		if (count > 1) {
			throw new IllegalStateException(
					"The table '" + this.getTableName() + "' PRIMARY KEY constraint has been damaged, please fix it.");
		}
	}
	
	
	public void delete(String originalVoucherCreationId, int version) throws Exception{
	
		String methodName="delete(String originalVoucherCreationId, int version)";
		assertMethodArgumentNotNull(originalVoucherCreationId, methodName, "originalVoucherCreationId");
		assertMethodIntArgumentGreaterThan(version,0, methodName, "options");
		
	
		String SQL=this.getDeleteSQL();
		Object [] parameters=new Object[]{originalVoucherCreationId,version};
		int affectedNumber = singleUpdate(SQL,parameters);
		if(affectedNumber == 1){
			return ; //Delete successfully
		}
		if(affectedNumber == 0){
			handleDeleteOneError(originalVoucherCreationId,version);
		}
		
	
	}
	
	
	
	
	

	public OriginalVoucherCreation disconnectFromAll(String originalVoucherCreationId, int version) throws Exception{
	
		
		OriginalVoucherCreation originalVoucherCreation = loadInternalOriginalVoucherCreation(OriginalVoucherCreationTable.withId(originalVoucherCreationId), emptyOptions());
		originalVoucherCreation.clearFromAll();
		this.saveOriginalVoucherCreation(originalVoucherCreation);
		return originalVoucherCreation;
		
	
	}
	
	@Override
	protected String[] getNormalColumnNames() {

		return OriginalVoucherCreationTable.NORMAL_CLOUMNS;
	}
	@Override
	protected String getName() {
		
		return "original_voucher_creation";
	}
	@Override
	protected String getBeanName() {
		
		return "originalVoucherCreation";
	}
	
	
	
	
	
	protected boolean checkOptions(Map<String,Object> options, String optionToCheck){
	
 		return OriginalVoucherCreationTokens.checkOptions(options, optionToCheck);
	
	}


		
	
	protected boolean isExtractOriginalVoucherListEnabled(Map<String,Object> options){		
 		return checkOptions(options,OriginalVoucherCreationTokens.ORIGINAL_VOUCHER_LIST);
 	}
 	protected boolean isAnalyzeOriginalVoucherListEnabled(Map<String,Object> options){		 		
 		return OriginalVoucherCreationTokens.of(options).analyzeOriginalVoucherListEnabled();
 	}
	
	protected boolean isSaveOriginalVoucherListEnabled(Map<String,Object> options){
		return checkOptions(options, OriginalVoucherCreationTokens.ORIGINAL_VOUCHER_LIST);
		
 	}
 	
		

	

	protected OriginalVoucherCreationMapper getOriginalVoucherCreationMapper(){
		return new OriginalVoucherCreationMapper();
	}

	
	
	protected OriginalVoucherCreation extractOriginalVoucherCreation(AccessKey accessKey, Map<String,Object> loadOptions) throws Exception{
		try{
			OriginalVoucherCreation originalVoucherCreation = loadSingleObject(accessKey, getOriginalVoucherCreationMapper());
			return originalVoucherCreation;
		}catch(EmptyResultDataAccessException e){
			throw new OriginalVoucherCreationNotFoundException("OriginalVoucherCreation("+accessKey+") is not found!");
		}

	}

	
	

	protected OriginalVoucherCreation loadInternalOriginalVoucherCreation(AccessKey accessKey, Map<String,Object> loadOptions) throws Exception{
		
		OriginalVoucherCreation originalVoucherCreation = extractOriginalVoucherCreation(accessKey, loadOptions);

		
		if(isExtractOriginalVoucherListEnabled(loadOptions)){
	 		extractOriginalVoucherList(originalVoucherCreation, loadOptions);
 		}	
 		if(isAnalyzeOriginalVoucherListEnabled(loadOptions)){
	 		analyzeOriginalVoucherList(originalVoucherCreation, loadOptions);
 		}
 		
		
		return originalVoucherCreation;
		
	}

	
		
	protected void enhanceOriginalVoucherList(SmartList<OriginalVoucher> originalVoucherList,Map<String,Object> options){
		//extract multiple list from difference sources
		//Trying to use a single SQL to extract all data from database and do the work in java side, java is easier to scale to N ndoes;
	}
	
	protected OriginalVoucherCreation extractOriginalVoucherList(OriginalVoucherCreation originalVoucherCreation, Map<String,Object> options){
		
		
		if(originalVoucherCreation == null){
			return null;
		}
		if(originalVoucherCreation.getId() == null){
			return originalVoucherCreation;
		}

		
		
		SmartList<OriginalVoucher> originalVoucherList = getOriginalVoucherDAO().findOriginalVoucherByCreation(originalVoucherCreation.getId(),options);
		if(originalVoucherList != null){
			enhanceOriginalVoucherList(originalVoucherList,options);
			originalVoucherCreation.setOriginalVoucherList(originalVoucherList);
		}
		
		return originalVoucherCreation;
	
	}	
	
	protected OriginalVoucherCreation analyzeOriginalVoucherList(OriginalVoucherCreation originalVoucherCreation, Map<String,Object> options){
		
		
		if(originalVoucherCreation == null){
			return null;
		}
		if(originalVoucherCreation.getId() == null){
			return originalVoucherCreation;
		}

		
		
		SmartList<OriginalVoucher> originalVoucherList = originalVoucherCreation.getOriginalVoucherList();
		if(originalVoucherList != null){
			getOriginalVoucherDAO().analyzeOriginalVoucherByCreation(originalVoucherList, originalVoucherCreation.getId(), options);
			
		}
		
		return originalVoucherCreation;
	
	}	
	
		
		
 	
		
		
		

	

	protected OriginalVoucherCreation saveOriginalVoucherCreation(OriginalVoucherCreation  originalVoucherCreation){
		
		if(!originalVoucherCreation.isChanged()){
			return originalVoucherCreation;
		}
		
		
		String SQL=this.getSaveOriginalVoucherCreationSQL(originalVoucherCreation);
		//FIXME: how about when an item has been updated more than MAX_INT?
		Object [] parameters = getSaveOriginalVoucherCreationParameters(originalVoucherCreation);
		int affectedNumber = singleUpdate(SQL,parameters);
		if(affectedNumber != 1){
			throw new IllegalStateException("The save operation should return value = 1, while the value = "
				+ affectedNumber +"If the value = 0, that mean the target record has been updated by someone else!");
		}
		
		originalVoucherCreation.incVersion();
		return originalVoucherCreation;
	
	}
	public SmartList<OriginalVoucherCreation> saveOriginalVoucherCreationList(SmartList<OriginalVoucherCreation> originalVoucherCreationList,Map<String,Object> options){
		//assuming here are big amount objects to be updated.
		//First step is split into two groups, one group for update and another group for create
		Object [] lists=splitOriginalVoucherCreationList(originalVoucherCreationList);
		
		batchOriginalVoucherCreationCreate((List<OriginalVoucherCreation>)lists[CREATE_LIST_INDEX]);
		
		batchOriginalVoucherCreationUpdate((List<OriginalVoucherCreation>)lists[UPDATE_LIST_INDEX]);
		
		
		//update version after the list successfully saved to database;
		for(OriginalVoucherCreation originalVoucherCreation:originalVoucherCreationList){
			if(originalVoucherCreation.isChanged()){
				originalVoucherCreation.incVersion();
			}
			
		
		}
		
		
		return originalVoucherCreationList;
	}

	public SmartList<OriginalVoucherCreation> removeOriginalVoucherCreationList(SmartList<OriginalVoucherCreation> originalVoucherCreationList,Map<String,Object> options){
		
		
		super.removeList(originalVoucherCreationList, options);
		
		return originalVoucherCreationList;
		
		
	}
	
	protected List<Object[]> prepareOriginalVoucherCreationBatchCreateArgs(List<OriginalVoucherCreation> originalVoucherCreationList){
		
		List<Object[]> parametersList=new ArrayList<Object[]>();
		for(OriginalVoucherCreation originalVoucherCreation:originalVoucherCreationList ){
			Object [] parameters = prepareOriginalVoucherCreationCreateParameters(originalVoucherCreation);
			parametersList.add(parameters);
		
		}
		return parametersList;
		
	}
	protected List<Object[]> prepareOriginalVoucherCreationBatchUpdateArgs(List<OriginalVoucherCreation> originalVoucherCreationList){
		
		List<Object[]> parametersList=new ArrayList<Object[]>();
		for(OriginalVoucherCreation originalVoucherCreation:originalVoucherCreationList ){
			if(!originalVoucherCreation.isChanged()){
				continue;
			}
			Object [] parameters = prepareOriginalVoucherCreationUpdateParameters(originalVoucherCreation);
			parametersList.add(parameters);
		
		}
		return parametersList;
		
	}
	protected void batchOriginalVoucherCreationCreate(List<OriginalVoucherCreation> originalVoucherCreationList){
		String SQL=getCreateSQL();
		List<Object[]> args=prepareOriginalVoucherCreationBatchCreateArgs(originalVoucherCreationList);
		
		int affectedNumbers[] = batchUpdate(SQL, args);
		
	}
	
	
	protected void batchOriginalVoucherCreationUpdate(List<OriginalVoucherCreation> originalVoucherCreationList){
		String SQL=getUpdateSQL();
		List<Object[]> args=prepareOriginalVoucherCreationBatchUpdateArgs(originalVoucherCreationList);
		
		int affectedNumbers[] = batchUpdate(SQL, args);
		
		
		
	}
	
	
	
	static final int CREATE_LIST_INDEX=0;
	static final int UPDATE_LIST_INDEX=1;
	
	protected Object[] splitOriginalVoucherCreationList(List<OriginalVoucherCreation> originalVoucherCreationList){
		
		List<OriginalVoucherCreation> originalVoucherCreationCreateList=new ArrayList<OriginalVoucherCreation>();
		List<OriginalVoucherCreation> originalVoucherCreationUpdateList=new ArrayList<OriginalVoucherCreation>();
		
		for(OriginalVoucherCreation originalVoucherCreation: originalVoucherCreationList){
			if(isUpdateRequest(originalVoucherCreation)){
				originalVoucherCreationUpdateList.add( originalVoucherCreation);
				continue;
			}
			originalVoucherCreationCreateList.add(originalVoucherCreation);
		}
		
		return new Object[]{originalVoucherCreationCreateList,originalVoucherCreationUpdateList};
	}
	
	protected boolean isUpdateRequest(OriginalVoucherCreation originalVoucherCreation){
 		return originalVoucherCreation.getVersion() > 0;
 	}
 	protected String getSaveOriginalVoucherCreationSQL(OriginalVoucherCreation originalVoucherCreation){
 		if(isUpdateRequest(originalVoucherCreation)){
 			return getUpdateSQL();
 		}
 		return getCreateSQL();
 	}
 	
 	protected Object[] getSaveOriginalVoucherCreationParameters(OriginalVoucherCreation originalVoucherCreation){
 		if(isUpdateRequest(originalVoucherCreation) ){
 			return prepareOriginalVoucherCreationUpdateParameters(originalVoucherCreation);
 		}
 		return prepareOriginalVoucherCreationCreateParameters(originalVoucherCreation);
 	}
 	protected Object[] prepareOriginalVoucherCreationUpdateParameters(OriginalVoucherCreation originalVoucherCreation){
 		Object[] parameters = new Object[6];
 
 		parameters[0] = originalVoucherCreation.getWho();
 		parameters[1] = originalVoucherCreation.getComments();
 		parameters[2] = originalVoucherCreation.getMakeDate();		
 		parameters[3] = originalVoucherCreation.nextVersion();
 		parameters[4] = originalVoucherCreation.getId();
 		parameters[5] = originalVoucherCreation.getVersion();
 				
 		return parameters;
 	}
 	protected Object[] prepareOriginalVoucherCreationCreateParameters(OriginalVoucherCreation originalVoucherCreation){
		Object[] parameters = new Object[4];
		String newOriginalVoucherCreationId=getNextId();
		originalVoucherCreation.setId(newOriginalVoucherCreationId);
		parameters[0] =  originalVoucherCreation.getId();
 
 		parameters[1] = originalVoucherCreation.getWho();
 		parameters[2] = originalVoucherCreation.getComments();
 		parameters[3] = originalVoucherCreation.getMakeDate();		
 				
 		return parameters;
 	}
 	
	protected OriginalVoucherCreation saveInternalOriginalVoucherCreation(OriginalVoucherCreation originalVoucherCreation, Map<String,Object> options){
		
		saveOriginalVoucherCreation(originalVoucherCreation);

		
		if(isSaveOriginalVoucherListEnabled(options)){
	 		saveOriginalVoucherList(originalVoucherCreation, options);
	 		//removeOriginalVoucherList(originalVoucherCreation, options);
	 		//Not delete the record
	 		
 		}		
		
		return originalVoucherCreation;
		
	}
	
	
	
	//======================================================================================
	

	
	public OriginalVoucherCreation planToRemoveOriginalVoucherList(OriginalVoucherCreation originalVoucherCreation, String originalVoucherIds[], Map<String,Object> options)throws Exception{
	
		MultipleAccessKey key = new MultipleAccessKey();
		key.put(OriginalVoucher.CREATION_PROPERTY, originalVoucherCreation.getId());
		key.put(OriginalVoucher.ID_PROPERTY, originalVoucherIds);
		
		SmartList<OriginalVoucher> externalOriginalVoucherList = getOriginalVoucherDAO().
				findOriginalVoucherWithKey(key, options);
		if(externalOriginalVoucherList == null){
			return originalVoucherCreation;
		}
		if(externalOriginalVoucherList.isEmpty()){
			return originalVoucherCreation;
		}
		
		for(OriginalVoucher originalVoucherItem: externalOriginalVoucherList){

			originalVoucherItem.clearFromAll();
		}
		
		
		SmartList<OriginalVoucher> originalVoucherList = originalVoucherCreation.getOriginalVoucherList();		
		originalVoucherList.addAllToRemoveList(externalOriginalVoucherList);
		return originalVoucherCreation;	
	
	}


	//disconnect OriginalVoucherCreation with belongs_to in OriginalVoucher
	public OriginalVoucherCreation planToRemoveOriginalVoucherListWithBelongsTo(OriginalVoucherCreation originalVoucherCreation, String belongsToId, Map<String,Object> options)throws Exception{
				//SmartList<ThreadLike> toRemoveThreadLikeList = threadLikeList.getToRemoveList();
		//the list will not be null here, empty, maybe
		//getThreadLikeDAO().removeThreadLikeList(toRemoveThreadLikeList,options);
		
		MultipleAccessKey key = new MultipleAccessKey();
		key.put(OriginalVoucher.CREATION_PROPERTY, originalVoucherCreation.getId());
		key.put(OriginalVoucher.BELONGS_TO_PROPERTY, belongsToId);
		
		SmartList<OriginalVoucher> externalOriginalVoucherList = getOriginalVoucherDAO().
				findOriginalVoucherWithKey(key, options);
		if(externalOriginalVoucherList == null){
			return originalVoucherCreation;
		}
		if(externalOriginalVoucherList.isEmpty()){
			return originalVoucherCreation;
		}
		
		for(OriginalVoucher originalVoucherItem: externalOriginalVoucherList){
			originalVoucherItem.clearBelongsTo();
			originalVoucherItem.clearCreation();
			
		}
		
		
		SmartList<OriginalVoucher> originalVoucherList = originalVoucherCreation.getOriginalVoucherList();		
		originalVoucherList.addAllToRemoveList(externalOriginalVoucherList);
		return originalVoucherCreation;
	}
	
	public int countOriginalVoucherListWithBelongsTo(String originalVoucherCreationId, String belongsToId, Map<String,Object> options)throws Exception{
				//SmartList<ThreadLike> toRemoveThreadLikeList = threadLikeList.getToRemoveList();
		//the list will not be null here, empty, maybe
		//getThreadLikeDAO().removeThreadLikeList(toRemoveThreadLikeList,options);

		MultipleAccessKey key = new MultipleAccessKey();
		key.put(OriginalVoucher.CREATION_PROPERTY, originalVoucherCreationId);
		key.put(OriginalVoucher.BELONGS_TO_PROPERTY, belongsToId);
		
		int count = getOriginalVoucherDAO().countOriginalVoucherWithKey(key, options);
		return count;
	}
	

		
	protected OriginalVoucherCreation saveOriginalVoucherList(OriginalVoucherCreation originalVoucherCreation, Map<String,Object> options){
		
		
		
		
		SmartList<OriginalVoucher> originalVoucherList = originalVoucherCreation.getOriginalVoucherList();
		if(originalVoucherList == null){
			//null list means nothing
			return originalVoucherCreation;
		}
		SmartList<OriginalVoucher> mergedUpdateOriginalVoucherList = new SmartList<OriginalVoucher>();
		
		
		mergedUpdateOriginalVoucherList.addAll(originalVoucherList); 
		if(originalVoucherList.getToRemoveList() != null){
			//ensures the toRemoveList is not null
			mergedUpdateOriginalVoucherList.addAll(originalVoucherList.getToRemoveList());
			originalVoucherList.removeAll(originalVoucherList.getToRemoveList());
			//OK for now, need fix later
		}

		//adding new size can improve performance
	
		getOriginalVoucherDAO().saveOriginalVoucherList(mergedUpdateOriginalVoucherList,options);
		
		if(originalVoucherList.getToRemoveList() != null){
			originalVoucherList.removeAll(originalVoucherList.getToRemoveList());
		}
		
		
		return originalVoucherCreation;
	
	}
	
	protected OriginalVoucherCreation removeOriginalVoucherList(OriginalVoucherCreation originalVoucherCreation, Map<String,Object> options){
	
	
		SmartList<OriginalVoucher> originalVoucherList = originalVoucherCreation.getOriginalVoucherList();
		if(originalVoucherList == null){
			return originalVoucherCreation;
		}	
	
		SmartList<OriginalVoucher> toRemoveOriginalVoucherList = originalVoucherList.getToRemoveList();
		
		if(toRemoveOriginalVoucherList == null){
			return originalVoucherCreation;
		}
		if(toRemoveOriginalVoucherList.isEmpty()){
			return originalVoucherCreation;// Does this mean delete all from the parent object?
		}
		//Call DAO to remove the list
		
		getOriginalVoucherDAO().removeOriginalVoucherList(toRemoveOriginalVoucherList,options);
		
		return originalVoucherCreation;
	
	}
	
	

 	
 	
	
	
	
		

	public OriginalVoucherCreation present(OriginalVoucherCreation originalVoucherCreation,Map<String, Object> options){
	
		presentOriginalVoucherList(originalVoucherCreation,options);

		return originalVoucherCreation;
	
	}
		
	//Using java8 feature to reduce the code significantly
 	protected OriginalVoucherCreation presentOriginalVoucherList(
			OriginalVoucherCreation originalVoucherCreation,
			Map<String, Object> options) {

		SmartList<OriginalVoucher> originalVoucherList = originalVoucherCreation.getOriginalVoucherList();		
				SmartList<OriginalVoucher> newList= presentSubList(originalVoucherCreation.getId(),
				originalVoucherList,
				options,
				getOriginalVoucherDAO()::countOriginalVoucherByCreation,
				getOriginalVoucherDAO()::findOriginalVoucherByCreation
				);

		
		originalVoucherCreation.setOriginalVoucherList(newList);
		

		return originalVoucherCreation;
	}			
		

	
    public SmartList<OriginalVoucherCreation> requestCandidateOriginalVoucherCreationForOriginalVoucher(RetailscmUserContext userContext, String ownerClass, String id, String filterKey, int pageNo, int pageSize) throws Exception {
        // NOTE: by default, ignore owner info, just return all by filter key.
		// You need override this method if you have different candidate-logic
		return findAllCandidateByFilter(OriginalVoucherCreationTable.COLUMN_WHO, filterKey, pageNo, pageSize, getOriginalVoucherCreationMapper());
    }
		

	protected String getTableName(){
		return OriginalVoucherCreationTable.TABLE_NAME;
	}
	
	
	
	public void enhanceList(List<OriginalVoucherCreation> originalVoucherCreationList) {		
		this.enhanceListInternal(originalVoucherCreationList, this.getOriginalVoucherCreationMapper());
	}
	
	
	// 需要一个加载引用我的对象的enhance方法:OriginalVoucher的creation的OriginalVoucherList
	public SmartList<OriginalVoucher> loadOurOriginalVoucherList(RetailscmUserContext userContext, List<OriginalVoucherCreation> us, Map<String,Object> options) throws Exception{
		if (us == null || us.isEmpty()){
			return new SmartList<>();
		}
		Set<String> ids = us.stream().map(it->it.getId()).collect(Collectors.toSet());
		MultipleAccessKey key = new MultipleAccessKey();
		key.put(OriginalVoucher.CREATION_PROPERTY, ids.toArray(new String[ids.size()]));
		SmartList<OriginalVoucher> loadedObjs = userContext.getDAOGroup().getOriginalVoucherDAO().findOriginalVoucherWithKey(key, options);
		Map<String, List<OriginalVoucher>> loadedMap = loadedObjs.stream().collect(Collectors.groupingBy(it->it.getCreation().getId()));
		us.forEach(it->{
			String id = it.getId();
			List<OriginalVoucher> loadedList = loadedMap.get(id);
			if (loadedList == null || loadedList.isEmpty()) {
				return;
			}
			SmartList<OriginalVoucher> loadedSmartList = new SmartList<>();
			loadedSmartList.addAll(loadedList);
			it.setOriginalVoucherList(loadedSmartList);
		});
		return loadedObjs;
	}
	
	
	@Override
	public void collectAndEnhance(BaseEntity ownerEntity) {
		List<OriginalVoucherCreation> originalVoucherCreationList = ownerEntity.collectRefsWithType(OriginalVoucherCreation.INTERNAL_TYPE);
		this.enhanceList(originalVoucherCreationList);
		
	}
	
	@Override
	public SmartList<OriginalVoucherCreation> findOriginalVoucherCreationWithKey(MultipleAccessKey key,
			Map<String, Object> options) {
		
  		return queryWith(key, options, getOriginalVoucherCreationMapper());

	}
	@Override
	public int countOriginalVoucherCreationWithKey(MultipleAccessKey key,
			Map<String, Object> options) {
		
  		return countWith(key, options);

	}
	public Map<String, Integer> countOriginalVoucherCreationWithGroupKey(String groupKey, MultipleAccessKey filterKey,
			Map<String, Object> options) {
			
  		return countWithGroup(groupKey, filterKey, options);

	}
	
	@Override
	public SmartList<OriginalVoucherCreation> queryList(String sql, Object... parameters) {
	    return this.queryForList(sql, parameters, this.getOriginalVoucherCreationMapper());
	}
	
	
    
	public Map<String, Integer> countBySql(String sql, Object[] params) {
		if (params == null || params.length == 0) {
			return new HashMap<>();
		}
		List<Map<String, Object>> result = this.getJdbcTemplateObject().queryForList(sql, params);
		if (result == null || result.isEmpty()) {
			return new HashMap<>();
		}
		Map<String, Integer> cntMap = new HashMap<>();
		for (Map<String, Object> data : result) {
			String key = (String) data.get("id");
			Number value = (Number) data.get("count");
			cntMap.put(key, value.intValue());
		}
		this.logSQLAndParameters("countBySql", sql, params, cntMap.size() + " Counts");
		return cntMap;
	}

	public Integer singleCountBySql(String sql, Object[] params) {
		Integer cnt = this.getJdbcTemplateObject().queryForObject(sql, params, Integer.class);
		logSQLAndParameters("singleCountBySql", sql, params, cnt + "");
		return cnt;
	}

	public BigDecimal summaryBySql(String sql, Object[] params) {
		BigDecimal cnt = this.getJdbcTemplateObject().queryForObject(sql, params, BigDecimal.class);
		logSQLAndParameters("summaryBySql", sql, params, cnt + "");
		return cnt == null ? BigDecimal.ZERO : cnt;
	}

	public <T> List<T> queryForList(String sql, Object[] params, Class<T> claxx) {
		List<T> result = this.getJdbcTemplateObject().queryForList(sql, params, claxx);
		logSQLAndParameters("queryForList", sql, params, result.size() + " items");
		return result;
	}

	public Map<String, Object> queryForMap(String sql, Object[] params) throws DataAccessException {
		Map<String, Object> result = null;
		try {
			result = this.getJdbcTemplateObject().queryForMap(sql, params);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			// 空结果，返回null
		}
		logSQLAndParameters("queryForObject", sql, params, result == null ? "not found" : String.valueOf(result));
		return result;
	}

	public <T> T queryForObject(String sql, Object[] params, Class<T> claxx) throws DataAccessException {
		T result = null;
		try {
			result = this.getJdbcTemplateObject().queryForObject(sql, params, claxx);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			// 空结果，返回null
		}
		logSQLAndParameters("queryForObject", sql, params, result == null ? "not found" : String.valueOf(result));
		return result;
	}

	public List<Map<String, Object>> queryAsMapList(String sql, Object[] params) {
		List<Map<String, Object>> result = getJdbcTemplateObject().queryForList(sql, params);
		logSQLAndParameters("queryAsMapList", sql, params, result.size() + " items");
		return result;
	}

	public synchronized int updateBySql(String sql, Object[] params) {
		int result = getJdbcTemplateObject().update(sql, params);
		logSQLAndParameters("updateBySql", sql, params, result + " items");
		return result;
	}

	public void execSqlWithRowCallback(String sql, Object[] args, RowCallbackHandler callback) {
		getJdbcTemplateObject().query(sql, args, callback);
	}

	public void executeSql(String sql) {
		logSQLAndParameters("executeSql", sql, new Object[] {}, "");
		getJdbcTemplateObject().execute(sql);
	}


}


