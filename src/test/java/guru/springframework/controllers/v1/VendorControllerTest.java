package guru.springframework.controllers.v1;

import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.controllers.RestResponseEntityExceptionHandler;
import guru.springframework.services.ResourceNotFoundException;
import guru.springframework.services.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static guru.springframework.api.v1.mapper.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VendorControllerTest {

    private static final long ID = 1L;
    private static final String FIRSTNAME = "John";

    @Mock
    private VendorService vendorService;

    @InjectMocks
    private VendorController vendorController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vendorController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    void getAllVendors() throws Exception {
        VendorDTO vendorDTO1 = new VendorDTO();
        vendorDTO1.setName(FIRSTNAME);
        vendorDTO1.setVendorUrl(vendorController.BASE_URL + "/1");

        VendorDTO vendorDTO2 = new VendorDTO();
        vendorDTO2.setName("Anna");

        List<VendorDTO> vendorDTOs = Arrays.asList(vendorDTO1, vendorDTO2);
        when(vendorService.getAllVendors()).thenReturn(vendorDTOs);

        mockMvc.perform(get(vendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    void getVendorById() throws Exception {
        VendorDTO vendorDTO1 = new VendorDTO();
        vendorDTO1.setName(FIRSTNAME);
        vendorDTO1.setVendorUrl(vendorController.BASE_URL + "/1");

        when(vendorService.getVendorById(ID)).thenReturn(vendorDTO1);
        mockMvc.perform(get(vendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(FIRSTNAME)))
                .andExpect(jsonPath("$.vendor_url", equalTo(vendorController.BASE_URL + "/1")));
    }

    @Test
    void createNewVendor() throws Exception {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName("Fred");

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendorDTO.getName());
        returnDTO.setVendorUrl(vendorController.BASE_URL + "/1");

        when(vendorService.createNewVendor(vendorDTO)).thenReturn(returnDTO);

        mockMvc.perform(post(vendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("Fred")))
                .andExpect(jsonPath("$.vendor_url", equalTo(vendorController.BASE_URL + "/1")));
    }

    @Test
    void testUpdateVendor() throws Exception {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName("Fred");

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendorDTO.getName());
        returnDTO.setVendorUrl(vendorController.BASE_URL + "/1");

        when(vendorService.updateVendorByDTO(anyLong(), any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(put(vendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Fred")))
                .andExpect(jsonPath("$.vendor_url", equalTo(vendorController.BASE_URL + "/1")));
    }

    @Test
    void testPatchVendor() throws Exception {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName("Fred");

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendorDTO.getName());
        returnDTO.setVendorUrl(vendorController.BASE_URL + "/1");

        when(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(patch(vendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Fred")))
                .andExpect(jsonPath("$.vendor_url", equalTo(vendorController.BASE_URL + "/1")));
    }

    @Test
    void testDeleteVendor() throws Exception {

        mockMvc.perform(delete(vendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        when(vendorService.getVendorById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(vendorController.BASE_URL + "/222")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}