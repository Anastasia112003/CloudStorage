package com.example.cloudstorage.utils;

import com.example.cloudstorage.dtos.FileDto;
import com.example.cloudstorage.model.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    File toEntity(FileDto dto);

    FileDto toDto(File file);

}
