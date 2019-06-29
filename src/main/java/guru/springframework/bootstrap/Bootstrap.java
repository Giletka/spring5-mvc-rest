package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.Customer;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;


    public Bootstrap(CategoryRepository categoryRepository, CustomerRepository customerRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        initCategories();
        initCustomers();
    }

    private void initCustomers() {
        Customer smith = new Customer();
        smith.setFirstname("John");
        smith.setLastname("Smith");

        Customer holms = new Customer();
        holms.setFirstname("Anna");
        holms.setLastname("Holms");

        Customer brown = new Customer();
        brown.setFirstname("Chuck");
        brown.setLastname("Brown");


        customerRepository.save(smith);
        customerRepository.save(holms);
        customerRepository.save(brown);

        System.out.println("Customers Loaded = " + customerRepository.count());
    }

    private void initCategories() {
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
        nuts.setName("Nuts");

        categoryRepository.save(fruits);
        categoryRepository.save(dried);
        categoryRepository.save(fresh);
        categoryRepository.save(exotic);
        categoryRepository.save(nuts);

        System.out.println("Categories Loaded = " + categoryRepository.count());
    }
}
