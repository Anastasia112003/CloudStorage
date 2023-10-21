package com.example.cloudstorage.controller;

import com.example.cloudstorage.dtos.FileDto;
import com.example.cloudstorage.exception.WrongInfoException;
import com.example.cloudstorage.model.File;
import com.example.cloudstorage.service.CloudService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;
import java.util.List;

@RestController
public class CloudController {

    private final CloudService cloudService;

    public CloudController(CloudService cloudService) {
        this.cloudService = cloudService;
    }

    @PostMapping("/file")
    @PreAuthorize("hasAuthority('ADMIN_ROLE') or hasAuthority('USER_ROLE')")

    public ResponseEntity<Void> addFile(@NotNull @RequestParam("file") File file, @RequestParam("filename") String fileName) {

        try {
            cloudService.addFile(file);
        } catch (WrongInfoException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/file")
    @PreAuthorize("hasAuthority('ADMIN_ROLE') or hasAuthority('USER_ROLE')")
    public ResponseEntity<byte[]> getFile(@RequestParam("filename") String fileName) {
        FileDto fileDto = cloudService.getFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Arrays.toString(fileDto.getContent())))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getName() + "\"")
                .body(fileDto.getContent());
    }

    @PutMapping("/file")
    @PreAuthorize("hasAuthority('ADMIN_ROLE') or hasAuthority('USER_ROLE')")
    public ResponseEntity<Void> renameFile(@RequestParam("filename") String fileName, @RequestBody File file) {
        cloudService.renameFile(fileName, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    @PreAuthorize("hasAuthority('ADMIN_ROLE') or hasAuthority('USER_ROLE')")
    public ResponseEntity<Void> deleteFile(@RequestParam("filename") String fileName) {
        cloudService.deleteFileByName(fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN_ROLE') or hasAuthority('USER_ROLE')")
    public ResponseEntity<List<FileDto>> getAllFiles(@RequestParam int limit) {
        return new ResponseEntity<>(cloudService.getAllFiles(limit), HttpStatus.OK);
    }

}
