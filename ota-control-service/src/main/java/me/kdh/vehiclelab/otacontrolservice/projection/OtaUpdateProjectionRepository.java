package me.kdh.vehiclelab.otacontrolservice.projection;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OtaUpdateProjectionRepository extends MongoRepository<OtaUpdateProjection, Long> {

    List<OtaUpdateProjection> findAllByOrderByIdDesc();
}
