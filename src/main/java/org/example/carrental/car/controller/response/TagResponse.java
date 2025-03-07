package org.example.carrental.car.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.carrental.car.domain.Tag;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagResponse {

    private Long id;
    private String name;

    public TagResponse(Tag tag){
        this.id = tag.getId();
        this.name = tag.getName();
    }

}