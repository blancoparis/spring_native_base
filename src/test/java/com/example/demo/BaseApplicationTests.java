package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MimeTypeUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("Unitarios")
@AutoConfigureMockMvc
@SpringBootTest
class BaseApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired private ApplicationAvailability applicationAvailability;

	@Autowired ApplicationContext context;
	@DisplayName(" Leventar el contenedor ")
	@Test
	void contextLoads() {
	}

	@DisplayName(" Test del hola mundo ")
	@Test
	void holaMundo() throws Exception {
		var body = mockMvc.perform(
					get("/").accept(MimeTypeUtils.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn();
		assertEquals("hola mundo 11",body.getResponse().getContentAsString(),"El texto del hola mundo 2");

	}

	/**
	 * Validamos el status
	 */
	@DisplayName("Status")
	@Test
	void testStatus() throws Exception {
		var body = mockMvc.perform(
						get("/status").accept(MimeTypeUtils.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("OK"))
				.andExpect(jsonPath("$.env").hasJsonPath())
				.andExpect(jsonPath("$.springVersion").value(SpringVersion.getVersion()))
				.andExpect(jsonPath("$.springBootVersion").value(SpringBootVersion.getVersion()))
				.andExpect(jsonPath("$.jdk").hasJsonPath())
				.andExpect(jsonPath("$.java").value("21"))
				;

		System.out.println("Body" + body);
	}

	@DisplayName("Gestionar la disponiblidad de la aplicacion")
	@Test
	void testDisponibilidadKubernetes(){
		assertThat(applicationAvailability.getLivenessState())
				.isEqualTo(LivenessState.CORRECT);
		assertThat(applicationAvailability.getReadinessState())
				.isEqualTo(ReadinessState.ACCEPTING_TRAFFIC);
		assertThat(applicationAvailability.getState(ReadinessState.class))
				.isEqualTo(ReadinessState.ACCEPTING_TRAFFIC);
	}


}
