package com.example.demo;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import java.net.URI;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestPart;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.Parameters;
import org.springframework.restdocs.operation.RequestCookie;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class DemoApplicationTests {

	@Autowired()
	private WebTestClient webTestClient;
	
	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
	
	@Test
	public void contextLoads() {
	}

	@Before
	public void init() throws Exception {
		final URIUpdaterPreprocessor preprocessor = new URIUpdaterPreprocessor();
		webTestClient = webTestClient.mutate()
			.filter((documentationConfiguration(this.restDocumentation)
					.operationPreprocessors()
						.withRequestDefaults(preprocessor)
						.withResponseDefaults(prettyPrint()))
					)
			.build();
	}
	
	@Test
	public void testAboutApi() {
		webTestClient.get()
		.uri("/api/about")
		.exchange()
		.expectStatus().isOk()
		.expectBody().consumeWith(document("testAboutApi"));
	}
	
	private static final class URIUpdaterPreprocessor
		implements OperationPreprocessor {

		@Override
		public OperationRequest preprocess(OperationRequest request) {
			return new URIUpdaterOperationRequest(request);
		}

		@Override
		public OperationResponse preprocess(OperationResponse response) {
			return response;
		}
		
	}

	private static final class URIUpdaterOperationRequest
		implements OperationRequest {
	
		private OperationRequest delegate;
		
		public URIUpdaterOperationRequest(OperationRequest request) {
			delegate = request;
		}
		
		public byte[] getContent() {
			return delegate.getContent();
		}
	
		public String getContentAsString() {
			return delegate.getContentAsString();
		}
	
		public HttpHeaders getHeaders() {
			return delegate.getHeaders();
		}
	
		public HttpMethod getMethod() {
			return delegate.getMethod();
		}
	
		public Parameters getParameters() {
			return delegate.getParameters();
		}
	
		public Collection<OperationRequestPart> getParts() {
			return delegate.getParts();
		}
	
		public URI getUri() {
			URI sourceUri = delegate.getUri();
			UriComponentsBuilder builder = UriComponentsBuilder.fromUri(sourceUri);
			return builder
				.host(sourceUri.getHost())
				.replacePath("/service/foo"+sourceUri.getPath())
				.build().toUri();
		}
	
		public Collection<RequestCookie> getCookies() {
			return delegate.getCookies();
		}
	}
}

