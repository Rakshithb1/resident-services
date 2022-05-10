package io.mosip.resident.test.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import io.mosip.kernel.core.crypto.spi.CryptoCoreSpec;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.resident.controller.ProxyMasterdataController;
import io.mosip.resident.helper.ObjectStoreHelper;
import io.mosip.resident.service.DocumentService;
import io.mosip.resident.service.ProxyMasterdataService;
import io.mosip.resident.service.ResidentVidService;
import io.mosip.resident.test.ResidentTestBootApplication;
import io.mosip.resident.util.AuditUtil;

/**
 * Resident proxy masterdata controller test class.
 * 
 * @author Ritik Jain
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResidentTestBootApplication.class)
@AutoConfigureMockMvc
public class ProxyMasterdataControllerTest {

	@MockBean
	private ProxyMasterdataService proxyMasterdataService;

	@Mock
	private AuditUtil auditUtil;

	@MockBean
	@Qualifier("selfTokenRestTemplate")
	private RestTemplate residentRestTemplate;
	
	@MockBean
	private ResidentVidService vidService;

	@MockBean
	private CryptoCoreSpec<byte[], byte[], SecretKey, PublicKey, PrivateKey, String> encryptor;

	@InjectMocks
	private ProxyMasterdataController proxyMasterdataController;
	
	@MockBean
	private DocumentService docService;
	
	@MockBean
	private ObjectStoreHelper objectStore;

	@Autowired
	private MockMvc mockMvc;

	private ResponseWrapper responseWrapper;

	@Before
	public void setUp() throws Exception {
		responseWrapper = new ResponseWrapper<>();
		responseWrapper.setVersion("v1");
		responseWrapper.setId("1");
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(proxyMasterdataController).build();
		Mockito.doNothing().when(auditUtil).setAuditRequestDto(Mockito.any());
	}

	@Test
	public void testGetValidDocumentByLangCode() throws Exception {
		Mockito.when(proxyMasterdataService.getValidDocumentByLangCode(Mockito.anyString()))
				.thenReturn(responseWrapper);
		mockMvc.perform(MockMvcRequestBuilders.get("/proxy/masterdata/validdocuments/langCode"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetLocationHierarchyLevelByLangCode() throws Exception {
		Mockito.when(proxyMasterdataService.getLocationHierarchyLevelByLangCode(Mockito.anyString()))
				.thenReturn(responseWrapper);
		mockMvc.perform(MockMvcRequestBuilders.get("/proxy/masterdata/locationHierarchyLevels/langcode"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetImmediateChildrenByLocCodeAndLangCode() throws Exception {
		Mockito.when(proxyMasterdataService.getImmediateChildrenByLocCodeAndLangCode(Mockito.anyString(),
				Mockito.anyString())).thenReturn(responseWrapper);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/proxy/masterdata/locations/immediatechildren/locationcode/langcode"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetLocationDetailsByLocCodeAndLangCode() throws Exception {
		Mockito.when(
				proxyMasterdataService.getLocationDetailsByLocCodeAndLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseWrapper);
		mockMvc.perform(MockMvcRequestBuilders.get("/proxy/masterdata/locations/info/locationcode/langcode"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetCoordinateSpecificRegistrationCenters() throws Exception {
		Mockito.when(proxyMasterdataService.getCoordinateSpecificRegistrationCenters(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseWrapper);
		mockMvc.perform(MockMvcRequestBuilders.get(
				"/proxy/masterdata/getcoordinatespecificregistrationcenters/langcode/longitude/latitude/proximitydistance"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetApplicantValidDocument() throws Exception {
		Mockito.when(proxyMasterdataService.getApplicantValidDocument(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseWrapper);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/proxy/masterdata/applicanttype/applicantId/languages?languages=eng"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetRegistrationCentersByHierarchyLevel() throws Exception {
		Mockito.when(proxyMasterdataService.getRegistrationCentersByHierarchyLevel(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString())).thenReturn(responseWrapper);
		mockMvc.perform(MockMvcRequestBuilders
				.get("/proxy/masterdata/registrationcenters/langcode/hierarchylevel/names?name=14110"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetRegistrationCenterByHierarchyLevelAndTextPaginated() throws Exception {
		Mockito.when(proxyMasterdataService.getRegistrationCenterByHierarchyLevelAndTextPaginated(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(responseWrapper);
		mockMvc.perform(MockMvcRequestBuilders.get(
				"/proxy/masterdata/registrationcenters/page/langcode/hierarchylevel/name?pageNumber=0&pageSize=10&orderBy=desc&sortBy=createdDateTime"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetRegistrationCenterWorkingDays() throws Exception {
		Mockito.when(proxyMasterdataService.getRegistrationCenterWorkingDays(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseWrapper);
		mockMvc.perform(MockMvcRequestBuilders.get("/proxy/masterdata/workingdays/registrationCenterID/langCode"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetLatestIdSchema() throws Exception {
		Mockito.when(
				proxyMasterdataService.getLatestIdSchema(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseWrapper);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/proxy/masterdata/idschema/latest?schemaVersion=0&domain=domain&type=type"))
				.andExpect(status().isOk());
	}

}
