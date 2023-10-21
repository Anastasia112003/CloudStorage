package com.example.cloudstorage.controller;

import com.example.cloudstorage.dtos.FileDto;
import com.example.cloudstorage.model.File;
import com.example.cloudstorage.service.CloudServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CloudControllerTest {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private CloudServiceImpl cloudService;


    @Test
    void addFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test_file",
                MediaType.TEXT_PLAIN_VALUE,
                "Cloud_storage".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/file")
                        .file(multipartFile)
                        .param("test_file", "file")
                        .header("test-token", "Bearer test-token"))
                .andExpect(status().isOk());
    }


    @Test
    void getFile() throws Exception {
        FileDto fileDto = FileDto.builder()
                .content("Test_info".getBytes())
                .name("test_file")
                .build();
        Mockito.when(cloudService.getFile("test_file")).thenReturn(fileDto);
        mockMvc.perform(get("/file")
                        .param("test_file", "test_file.txt")
                        .header("test_token", "test_token"))
                .andExpect(status().isOk());
    }


    @Test
    void renameFile() throws Exception {
        File name = new File(1L, "newFileName.txt", new byte[35]);
        mockMvc.perform(put("/file")
                        .param("test-file", "test-file.txt")
                        .header(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(name.getFileName())))
                .andExpect(status().isOk());
    }

    @Test
    void deleteFile() throws Exception {
        mockMvc.perform(delete("/file")
                        .param("file-test", "file-test.txt")
                        .header("test-token", "test-token"))
                .andExpect(status().isOk());
    }


    @Test
    void getAllFiles() throws Exception {
        List<FileDto> listFile = List.of(
                FileDto.builder().content(new byte[35]).name("test-file-1.txt").build(),
                FileDto.builder().content(new byte[36]).name("test-file-2.txt").build(),
                FileDto.builder().content(new byte[37]).name("test-file-3.txt").build());
        Mockito.when(cloudService.getAllFiles(3)).thenReturn(listFile);
        mockMvc.perform(get("/list").header("test-token", "test-token").param("limit", "3"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().json(objectMapper.writeValueAsString(listFile)));
    }
}