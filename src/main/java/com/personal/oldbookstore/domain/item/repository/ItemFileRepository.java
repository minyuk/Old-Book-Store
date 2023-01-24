package com.personal.oldbookstore.domain.item.repository;

import com.personal.oldbookstore.domain.item.entity.ItemFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemFileRepository extends JpaRepository<ItemFile, Long> {

//    void deleteByFileName(String fileName);

}
