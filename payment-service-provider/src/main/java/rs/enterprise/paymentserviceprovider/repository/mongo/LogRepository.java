package rs.enterprise.paymentserviceprovider.repository.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import rs.enterprise.paymentserviceprovider.model.valueobjects.Log;


@Repository
public interface LogRepository extends MongoRepository<Log, String> {


    Page<Log> findAll(Pageable pageable);
}
