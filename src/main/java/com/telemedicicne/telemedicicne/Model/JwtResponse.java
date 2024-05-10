// Response 

package com.telemedicicne.telemedicicne.Model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtResponse {

    private String jwtToken;
    private String refreshToken;
    private String username;
    private String userId;

}
