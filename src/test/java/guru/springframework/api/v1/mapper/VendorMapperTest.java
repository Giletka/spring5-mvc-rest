package guru.springframework.api.v1.mapper;

import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.domain.Vendor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VendorMapperTest {

    private static final String NAME = "TestName_1";

    private VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Test
    void vendorToVendorDTO() {
        Vendor vendor = new Vendor();
        vendor.setName(NAME);

        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
        assertEquals(NAME, vendorDTO.getName());
    }

    @Test
    void vendorDTOToVendor() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        Vendor vendor = vendorMapper.vendorDTOToVendor(vendorDTO);
        assertEquals(NAME, vendor.getName());
    }
}