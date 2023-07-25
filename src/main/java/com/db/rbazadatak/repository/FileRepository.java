package com.db.rbazadatak.repository;

import com.db.rbazadatak.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

}
