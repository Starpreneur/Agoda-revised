package com.example.Klouud.AgodaJson.Repository;

import com.example.Klouud.AgodaJson.Enitity.RoomAvailabilityArchives;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomAvailabilityArchivesRepository extends MongoRepository<RoomAvailabilityArchives, ObjectId> {
}
