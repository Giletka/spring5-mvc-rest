package guru.springframework.services;

import guru.springframework.api.v1.mapper.VendorMapper;
import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.bootstrap.Bootstrap;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.CustomerRepository;
import guru.springframework.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class VendorServiceImplIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    VendorRepository vendorRepository;

    VendorService vendorService;

    @BeforeEach
    void setUp() {
        System.out.println("Loading Vendor Data");
        System.out.println(categoryRepository.findAll().size());

        Bootstrap bootstrap = new Bootstrap(categoryRepository, customerRepository, vendorRepository);
        bootstrap.run();

        vendorService = new VendorServiceImpl(VendorMapper.INSTANCE, vendorRepository);
    }

    @Test
    void patchVendorUpdateName() {
        String updateName = "UpdateName";
        long id = getVendorIdValue();

        Vendor originalVendor = vendorRepository.getOne(id);
        assertNotNull(originalVendor);
        String originalFirstname = originalVendor.getName();

        VendorDTO VendorDTO = new VendorDTO();
        VendorDTO.setName(updateName);

        vendorService.patchVendor(id, VendorDTO);

        Vendor updatedVendor = vendorRepository.findById(id).get();

        assertNotNull(updatedVendor);
        assertEquals(updateName, updatedVendor.getName());
        assertNotEquals(originalFirstname, updatedVendor.getName());
    }

    private Long getVendorIdValue() {
        List<Vendor> Vendors = vendorRepository.findAll();

        System.out.println("Vendors Found: " + Vendors.size());

        return Vendors.get(0).getId();
    }
}
