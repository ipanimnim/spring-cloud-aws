/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elasticspring.messaging.endpoint;

import org.elasticspring.core.support.documentation.RuntimeUse;
import org.elasticspring.messaging.config.annotation.NotificationMessage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class NotificationMessageHandlerMethodArgumentResolverTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void resolveArgument_wrongMessageType_reportsErrors() throws Exception {
		//Arrange
		this.expectedException.expect(IllegalArgumentException.class);
		this.expectedException.expectMessage("@NotificationMessage annotated parameters are only allowed");


		NotificationMessageHandlerMethodArgumentResolver resolver = new NotificationMessageHandlerMethodArgumentResolver();

		byte[] subscriptionRequestJsonContent = FileCopyUtils.copyToByteArray(new ClassPathResource("subscriptionConfirmation.json", getClass()).getInputStream());
		MockHttpServletRequest servletRequest = new MockHttpServletRequest();
		servletRequest.setContent(subscriptionRequestJsonContent);

		//Act
		resolver.resolveArgument(null, null, new ServletWebRequest(servletRequest), null);

		//Assert
	}

	@Test
	public void resolveArgument_notificationMessageTypeWithSubject_reportsErrors() throws Exception {
		//Arrange
		NotificationMessageHandlerMethodArgumentResolver resolver = new NotificationMessageHandlerMethodArgumentResolver();

		byte[] subscriptionRequestJsonContent = FileCopyUtils.copyToByteArray(new ClassPathResource("notificationMessage.json", getClass()).getInputStream());
		MockHttpServletRequest servletRequest = new MockHttpServletRequest();
		servletRequest.setContent(subscriptionRequestJsonContent);

		//Act
		Object argument = resolver.resolveArgument(null, null, new ServletWebRequest(servletRequest), null);

		//Assert
		assertEquals("asdasd", argument);
	}

	@Test
	public void supportsParameter_withWrongParameterType_shouldReturnFalse() throws Exception {
		// Arrange
		NotificationMessageHandlerMethodArgumentResolver resolver = new NotificationMessageHandlerMethodArgumentResolver();
		Method methodWithWrongParameterType = this.getClass().getDeclaredMethod("methodWithWrongParameterType", Integer.class);
		MethodParameter methodParameter = new MethodParameter(methodWithWrongParameterType, 0);

		// Act
		boolean supportsParameter = resolver.supportsParameter(methodParameter);

		// Assert
		assertFalse(supportsParameter);
	}

	@SuppressWarnings("EmptyMethod")
	@RuntimeUse
	private void methodWithWrongParameterType(@NotificationMessage Integer message) {
	}
}