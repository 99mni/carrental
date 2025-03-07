package org.example.carrental.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private long id;

    private String name;

}