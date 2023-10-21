package com.example.cloudstorage.service;

import com.example.cloudstorage.dtos.FileDto;
import com.example.cloudstorage.exception.WrongInfoException;
import com.example.cloudstorage.model.File;
import com.example.cloudstorage.model.User;
import com.example.cloudstorage.repository.FileRepository;
import com.example.cloudstorage.security.JWTToken;
import com.example.cloudstorage.utils.FileMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class CloudServiceImplTest {
    @MockBean
    private JWTToken jwtToken;
    @MockBean
    private FileRepository cloudRepository;
    @Autowired
    private CloudServiceImpl cloudService;
    private final static Long id = 1l;
    private final static int limit = 2;
    private File file;
    private User user;

    @BeforeEach
    public void init() {
        user = User.builder().id(1L).build();
        file = File.builder()
                .fileContent(new byte[35])
                .fileName("java.zip")
                .id(1L)
                .build();
    }

    @Test
    void getAllFiles() {
        List<File> list = List.of(
                File.builder().fileName("java.zip").fileContent(new byte[35]).build(),
                File.builder().fileName("kafka.zip").fileContent(new byte[198]).build(),
                File.builder().fileName("python.zip").fileContent(new byte[38]).build()
        );
        Mockito.doReturn(user).when(jwtToken.getAuthenticatedUser());
        Mockito.doReturn(list)
                .when(cloudRepository.findFilesByUserIdWithLimit(user.getId(), limit));
        List<FileDto> files = cloudService.getAllFiles(limit);
        Assertions.assertEquals("java.zip", files.get(0).getName());
    }

    @Test
    void deleteFileByName() {
        Mockito.doReturn(user).when(jwtToken.getAuthenticatedUser());
        Mockito.when(cloudRepository.findFileByUserIdAndFileName(id, "java.zip"));
        cloudService.deleteFileByName("java.zip");
        Mockito.verify(cloudRepository, Mockito.times(1)).deleteById(file.getId());
    }

    @Test
    void getFile() {
        Mockito.doReturn(user).when(jwtToken.getAuthenticatedUser());
        Mockito.when(cloudRepository.findFileByUserIdAndFileName(id, "java.zip"));
        FileDto fileDto = cloudService.getFile("java.zip");
        Assertions.assertEquals("java.zip", fileDto.getName());

    }

    @Test
    void renameFile() {
        Mockito.doReturn(user).when(jwtToken.getAuthenticatedUser());
        Mockito.when(cloudRepository.findFileByUserIdAndFileName(id, "java.zip"));
        cloudService.renameFile("java.zip", new File(id, "my_java.zip", new byte[35]));
        Mockito.verify(cloudRepository, Mockito.times(1)).save(file);

    }

    @Test
    void addFile() {
        Mockito.doReturn(user).when(jwtToken.getAuthenticatedUser());
        Mockito.when(cloudRepository.findFileByUserIdAndFileName(id, "java.zip"));
        File file = File.builder().id(1L).fileName("java.zip").fileContent(new byte[35]).build();
        try {
            cloudService.addFile(file);
        } catch (WrongInfoException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(new byte[35], file.getFileContent());

    }
}