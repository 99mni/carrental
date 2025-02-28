package org.example.carrental.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.carrental.entity.Tag;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagResponseDTO {

    private Long tagId;
    private String name;

    public static TagResponseDTO fromEntity(Tag tag) {
        return TagResponseDTO.builder()
                .tagId(tag.getTagId())
                .name(tag.getName())
                .build();
    }
}