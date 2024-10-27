package com.Fomichev.prak_5.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Fomichev.prak_5.model.FileEntity;

@Repository
public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
}
