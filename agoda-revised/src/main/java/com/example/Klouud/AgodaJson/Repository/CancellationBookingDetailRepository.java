package com.example.Klouud.AgodaJson.Repository;

import com.example.Klouud.AgodaJson.Model.CancellationBookingResponseDetailsFromAgoda;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancellationBookingDetailRepository extends MongoRepository<CancellationBookingResponseDetailsFromAgoda,String> {
}
