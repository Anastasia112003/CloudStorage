package com.example.cloudstorage.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDto {
    private Long id;
    private String name;
    private byte[] content;
}
