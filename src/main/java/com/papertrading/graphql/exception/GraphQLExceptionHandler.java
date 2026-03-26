package com.papertrading.graphql.exception;

import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

@Controller
public class GraphQLExceptionHandler {

	@GraphQlExceptionHandler({ IllegalArgumentException.class, AuthenticationException.class })
	public GraphQLError handleRuntimeException(RuntimeException ex, DataFetchingEnvironment environment) {
		return GraphqlErrorBuilder.newError(environment)
				.errorType(ErrorType.BAD_REQUEST)
				.message(ex.getMessage())
				.build();
	}
}
