package com.rs.elasticsearchservice.config.search;


import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;


public class CustomQueryBuilder {
	
	private static int maxEdits = 1;
	
	public static int getMaxEdits(){
		return maxEdits;
	}
	
	public static void setMaxEdits(int maxEdits){
		CustomQueryBuilder.maxEdits = maxEdits;
	}
	
	public static QueryBuilder buildQuery(SearchType queryType, String field, String value) throws IllegalArgumentException{
		String errorMessage = "";
		if(field == null || field.equals("")){
			errorMessage += "Field not specified";
		}
		if(value == null){
			if(!errorMessage.equals("")) errorMessage += "\n";
			errorMessage += "Value not specified";
		}
		if(!errorMessage.equals("")){
			throw new IllegalArgumentException(errorMessage);
		}

		QueryBuilder retVal = null;
		if(queryType.equals(SearchType.regular)){
			retVal = QueryBuilders.matchQuery(field, value);
		}else if(queryType.equals(SearchType.fuzzy)){
			retVal = QueryBuilders.fuzzyQuery(field, value).fuzziness(Fuzziness.fromEdits(maxEdits));
		}else if(queryType.equals(SearchType.prefix)){
			retVal = QueryBuilders.prefixQuery(field, value);
		}else if(queryType.equals(SearchType.range)){
			String[] values = value.split(" ");
			retVal = QueryBuilders.rangeQuery(field).from(values[0]).to(values[1]);
		}else{
			retVal = QueryBuilders.matchPhraseQuery(field, value);
		}
		
		return retVal;
	}

}
