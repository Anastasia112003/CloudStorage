package com.example.cloudstorage.service;

import com.example.cloudstorage.dtos.FileDto;
import com.example.cloudstorage.exception.WrongInfoException;
import com.example.cloudstorage.model.File;
import com.example.cloudstorage.repository.FileRepository;
import com.example.cloudstorage.security.JWTToken;
import com.example.cloudstorage.utils.FileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CloudServiceImpl implements CloudService {
    private final JWTToken jwtToken;
    private final FileRepository cloudRepository;
    private final FileMapper fileMapper;

    public CloudServiceImpl(JWTToken jwtToken, FileRepository cloudRepository, FileMapper fileMapper) {
        this.jwtToken = jwtToken;
        this.cloudRepository = cloudRepository;
        this.fileMapper = fileMapper;
    }

    @Override
    public List<FileDto> getAllFiles(int limit) {
        Long id = jwtToken.getAuthenticatedUser().getId();
        List<File> files = cloudRepository.findFilesByUserIdWithLimit(id, limit);

        return files.stream().map(file -> FileDto.builder()
                .name(file.getFileName())
                .content(file.getFileContent())
                .build())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean deleteFileByName(String name) {
        Long id = jwtToken.getAuthenticatedUser().getId();
        File file = cloudRepository.findFileByUserIdAndFileName(id, name);
        if (file.getFileName() == null && file.getId() == null) {
            return false;
        }
        cloudRepository.deleteById(id);
        return true;
    }

    @Override
    public FileDto getFile(String fileName) {
        Long id = jwtToken.getAuthenticatedUser().getId();
        File file = cloudRepository.findFileByUserIdAndFileName(id, fileName);
        return FileDto.builder()
                .content(file.getFileContent())
                .name(file.getFileName())
                .build();
    }

    @Transactional
    @Override
    public void renameFile(String fileName, File file) {
        Long id = jwtToken.getAuthenticatedUser().getId();
        File renamedFile = cloudRepository.findFileByUserIdAndFileName(id, fileName);
        cloudRepository.findFileByUserIdAndFileName(id, file.getFileName());

        renamedFile.setFileName(file.getFileName());
        cloudRepository.save(renamedFile);
    }

    @Transactional
    @Override
    public void addFile(File file) throws WrongInfoException {

        if (cloudRepository.findFileByUserIdAndFileName(file.getId(), file.getFileName()) == null) {
            File saveFile = File.builder().fileName(file.getFileName()).fileContent(file.getFileContent()).build();
            cloudRepository.save(saveFile);
        } else throw new WrongInfoException("Wrong file name");
    }

}
