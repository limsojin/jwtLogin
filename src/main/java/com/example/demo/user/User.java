package com.example.demo.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table ( name = "user" )
public class User
{
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private long id;
    private String username;
    private String email;
    private String passwd;
    private String roles; // USER, ADMIN
    private LocalDateTime createddate;

    public List<String> getRolesList ()
    {
        if ( this.roles.length () > 0 )
        {
            return Arrays.asList ( this.roles.split ( "," ) );
        }
        return new ArrayList<> ();
    }
}
