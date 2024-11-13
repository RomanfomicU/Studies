package com.fomic.dragonsrksp7.repository;

import com.fomic.dragonsrksp7.model.Dragon;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DragonRepository extends R2dbcRepository<Dragon, Long> {
}
