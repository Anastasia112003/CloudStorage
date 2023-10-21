package com.example.cloudstorage.service;

import com.example.cloudstorage.dtos.FileDto;
import com.example.cloudstorage.exception.WrongInfoException;
import com.example.cloudstorage.model.File;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CloudService {

    List<FileDto> getAllFiles(int limit);

    @Transactional
    boolean deleteFileByName(String name);

    FileDto getFile(String fileName);

    public void renameFile(String fileName, File file);

    void addFile(File file) throws WrongInfoException;
}
