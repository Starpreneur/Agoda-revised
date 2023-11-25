package com.example.Klouud.AgodaJson.Repository;

import com.example.Klouud.AgodaJson.Enitity.HotelsDetailedListByCitiesFeed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelsListByCitiesFeedRepository extends MongoRepository<HotelsDetailedListByCitiesFeed,String> {


}
