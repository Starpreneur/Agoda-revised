package com.example.Klouud.AgodaJson.Repository;

import com.example.Klouud.AgodaJson.Enitity.HotelIdsListByCityCacheV1;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelIdsListByCityCacheV1Repository extends MongoRepository<HotelIdsListByCityCacheV1,String> {
}
