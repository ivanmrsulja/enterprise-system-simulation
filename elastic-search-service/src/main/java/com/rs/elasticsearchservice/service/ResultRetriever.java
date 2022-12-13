package com.rs.elasticsearchservice.service;

import com.rs.elasticsearchservice.dto.CandidateApplicationDTO;
import com.rs.elasticsearchservice.model.CandidateApplication;
import com.rs.elasticsearchservice.model.RequiredHighlights;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultRetriever {
	
	private final ElasticsearchOperations template;

	@Autowired
	public ResultRetriever(ElasticsearchOperations template){
		this.template = template;
	}

	public List<CandidateApplicationDTO> getResults(org.elasticsearch.index.query.QueryBuilder query, List<RequiredHighlights> requiredHighlights) {
			
		List<CandidateApplicationDTO> results = new ArrayList<CandidateApplicationDTO>();

        var searchQuery = new NativeSearchQueryBuilder()
				.withQuery(query)
				.withHighlightFields(
						new HighlightBuilder.Field("name").fragmentSize(200),
						new HighlightBuilder.Field("surname").fragmentSize(200),
						new HighlightBuilder.Field("education").fragmentSize(200))
				.build();

		System.out.println(searchQuery.getQuery().toString());
        SearchHits<CandidateApplication> indexUnits = template.search(searchQuery, CandidateApplication.class, IndexCoordinates.of("applications"));

        for (var indexUnit : indexUnits.get().collect(Collectors.toList())) {
        	results.add(new CandidateApplicationDTO(indexUnit.getContent(), indexUnit.getHighlightFields()));
		}

		return results;
	}


}
