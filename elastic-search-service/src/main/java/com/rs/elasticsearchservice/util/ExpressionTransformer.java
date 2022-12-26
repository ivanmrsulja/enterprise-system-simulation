package com.rs.elasticsearchservice.util;

import com.rs.elasticsearchservice.model.RequiredHighlights;
import com.rs.elasticsearchservice.util.search.CustomQueryBuilder;
import com.rs.elasticsearchservice.util.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

@Component
public class ExpressionTransformer {

    private final Map<String, Integer> priorities = Map.of("AND",2, "OR", 1, "NOT", 3);

    public List<String> transformToPostFixNotation(List<String> expression) {
        var tokenStack = new Stack<String>();

        var postfixExpression = new ArrayList<String>();
        for (var token : expression) {
            if(!priorities.containsKey(token)) {
                postfixExpression.add(token);
            }
            else if(tokenStack.isEmpty()) {
                tokenStack.push(token);
            }
            else if(priorities.get(token) <= priorities.get(tokenStack.peek())) {
                postfixExpression.add(tokenStack.pop());
                tokenStack.push(token);
            } else {
                tokenStack.push(token);
            }
        }

        var limit = tokenStack.size();
        for(int i = 0; i < limit; i++) {
            postfixExpression.add(tokenStack.pop());
        }

        return postfixExpression;
    }

    public QueryBuilder buildQueryFromPostFixExpression(List<String> postfixExpression, List<RequiredHighlights> requiredHighlights) {
        var queryStack = new Stack<QueryBuilder>();

        for(var token : postfixExpression) {
            if(token.equalsIgnoreCase("AND")) {
                var other = queryStack.pop();
                queryStack.push(QueryBuilders.boolQuery()
                        .must(other)
                        .must(queryStack.pop()));
            } else if (token.equalsIgnoreCase("OR")) {
                var other = queryStack.pop();
                queryStack.push(QueryBuilders.boolQuery()
                        .should(other)
                        .should(queryStack.pop()));
            }  else if (token.equalsIgnoreCase("NOT")) {
                var other = queryStack.pop();
                queryStack.push(QueryBuilders.boolQuery()
                        .must(queryStack.pop())
                        .mustNot(other));
            } else {
                var fieldValueTuple = token.split(":");
                var searchType = SearchType.regular;
                if (fieldValueTuple[1].startsWith("\"") && fieldValueTuple[1].endsWith("\""))
                    searchType = SearchType.phrase;

                queryStack.push(CustomQueryBuilder.buildQuery(
                        searchType,
                        fieldValueTuple[0],
                        fieldValueTuple[1]));
                requiredHighlights.add(new RequiredHighlights(
                        fieldValueTuple[0],
                        fieldValueTuple[1]));
            }
        }

        return queryStack.pop();
    }
}
