package com.petproject.term_paper.repository;

import com.petproject.term_paper.entity.OwnerEntity;
import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.entity.enums.OwnerType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PropertyRepositoryTests {
    @Autowired private OwnerRepository owners;
    @Autowired private PropertyRepository properties;
    @Autowired private TransactionTemplate transactions;

    @Test
    void propertiesRemainSerializableAfterRepositoryTransactionCloses() {
        Long propertyId = transactions.execute(status -> {
            OwnerEntity owner = new OwnerEntity();
            owner.setOwnerType(OwnerType.INDIVIDUAL);
            owner.setFirstName("Integration");
            owner.setLastName("Owner");
            owner = owners.save(owner);

            PropertyEntity property = new PropertyEntity();
            property.setAddress("Integration test address");
            property.setOwner(owner);
            property.setImageUrls(List.of("https://example.test/image.jpg"));
            return properties.save(property).getId();
        });

        PropertyEntity detached = transactions.execute(status -> properties.findById(propertyId).orElseThrow());

        assertThat(detached.getImageUrls()).containsExactly("https://example.test/image.jpg");
        assertThat(detached.getOwner().getFirstName()).isEqualTo("Integration");
    }
}
