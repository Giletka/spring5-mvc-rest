package guru.springframework.services;

import guru.springframework.api.v1.mapper.VendorMapper;
import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.controllers.v1.VendorController;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorServiceImplTest {

    public static final String NAME = "Fenixxx";
    public static final long ID = 1L;
    @Mock
    private VendorRepository vendorRepository;

    private VendorService vendorService;

    @BeforeEach
    void setUp() {
        vendorService = new VendorServiceImpl(VendorMapper.INSTANCE, vendorRepository);
    }

    @Test
    void getAllVendors() {
        List<Vendor> vendors = Arrays.asList(new Vendor(), new Vendor(), new Vendor());

        when(vendorRepository.findAll()).thenReturn(vendors);

        List<VendorDTO> vendorDTOs = vendorService.getAllVendors();
        assertEquals(3, vendorDTOs.size());
    }

    @Test
    void getVendorById() {
        Vendor vendor = new Vendor();
        vendor.setId(ID);
        vendor.setName(NAME);

        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(vendor));

        VendorDTO vendorDTO = vendorService.getVendorById(ID);
        assertEquals(NAME, vendorDTO.getName());
        assertEquals(VendorController.BASE_URL + "/1", vendorDTO.getVendorUrl());
    }

    @Test
    void createNewVendor() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        Vendor savedVendor = new Vendor();
        savedVendor.setId(ID);
        savedVendor.setName(vendorDTO.getName());

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        VendorDTO returnVendorDTO = vendorService.createNewVendor(vendorDTO);
        assertEquals(NAME, returnVendorDTO.getName());
        assertEquals(VendorController.BASE_URL + "/1", returnVendorDTO.getVendorUrl());
    }

    @Test
    void updateVendorByDTO() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        Vendor savedVendor = new Vendor();
        savedVendor.setId(ID);
        savedVendor.setName(vendorDTO.getName());

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        VendorDTO returnVendorDTO = vendorService.updateVendorByDTO(ID, vendorDTO);
        assertEquals(NAME, returnVendorDTO.getName());
        assertEquals(VendorController.BASE_URL + "/1", returnVendorDTO.getVendorUrl());
    }

    @Test
    void deleteVendorById() {
        vendorService.deleteVendorById(ID);

        verify(vendorRepository, times(1)).deleteById(anyLong());
    }
}