// Security Authentication - Password - encrypt - decrypt 

package com.telemedicicne.telemedicicne.Model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtRequest {

    private String email;
    private String password;
}
