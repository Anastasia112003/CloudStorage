package com.example.cloudstorage.repository;

import com.example.cloudstorage.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    File findFileByUserIdAndFileName(Long userId, String fileName);

    List<File> findFilesByUserIdWithLimit(Long userId, int limit);

}
