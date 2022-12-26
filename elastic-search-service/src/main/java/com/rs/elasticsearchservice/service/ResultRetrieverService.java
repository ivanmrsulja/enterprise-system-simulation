package com.rs.elasticsearchservice.service;

import com.rs.elasticsearchservice.client.LocationIqClient;
import com.rs.elasticsearchservice.dto.CandidateApplicationDTO;
import com.rs.elasticsearchservice.model.AdvancedQuery;
import com.rs.elasticsearchservice.model.CandidateApplication;
import com.rs.elasticsearchservice.model.RequiredHighlights;
import com.rs.elasticsearchservice.util.ExpressionTransformer;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
public class ResultRetrieverService {
	
	private final ElasticsearchOperations template;

	private final ExpressionTransformer expressionTransformer;

	private final LocationIqClient locationIqClient;

	@Autowired
	public ResultRetrieverService(ElasticsearchOperations template,
								  ExpressionTransformer expressionTransformer,
								  LocationIqClient locationIqClient){
		this.template = template;
		this.expressionTransformer = expressionTransformer;
		this.locationIqClient = locationIqClient;
	}

	public List<CandidateApplicationDTO> getResults(AdvancedQuery advancedQuery) {
		var query = buildQuery(advancedQuery);
			
		List<CandidateApplicationDTO> results = new ArrayList<>();

        var searchQuery = new NativeSearchQueryBuilder()
				.withQuery(query)
				.withHighlightFields(
						new HighlightBuilder.Field("cv").fragmentSize(200),
						new HighlightBuilder.Field("letter").fragmentSize(200))
				.build();

        SearchHits<CandidateApplication> indexUnits = template
				.search(searchQuery, CandidateApplication.class, IndexCoordinates.of("applications"));

        for (var indexUnit : indexUnits.get().collect(Collectors.toList())) {
			var application = indexUnit.getContent();
        	results.add(
					new CandidateApplicationDTO(application.getName(),
							application.getSurname(),
							application.getEducation(),
							application.getCvPath(),
							application.getLetterPath(),
							indexUnit.getHighlightFields()));
		}

		return results;
	}

	private QueryBuilder buildQuery(AdvancedQuery advancedQuery) {
		var requiredHighlights = new ArrayList<RequiredHighlights>();

		var postfixExpression = expressionTransformer.transformToPostFixNotation(advancedQuery.getExpression());
		var finalQuery = expressionTransformer.buildQueryFromPostFixExpression(postfixExpression, requiredHighlights);

		var locationResponse = locationIqClient
				.forwardGeolocation("pk.1a9b36479a565979d1be82d2f63075b7", advancedQuery.getCityName(), "json").get(0);

		return QueryBuilders.boolQuery()
				.must(finalQuery)
				.must(QueryBuilders.geoDistanceQuery("location")
						.distance(advancedQuery.getDistance(), DistanceUnit.KILOMETERS)
						.point(locationResponse.getLat(),locationResponse.getLon()));
	}
}
