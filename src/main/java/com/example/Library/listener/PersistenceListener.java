package com.example.Library.listener;

import com.example.Library.model.entity.Identity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@Component
public class PersistenceListener {

    @PrePersist
    protected void setCreatedAtAndBy(Identity identity) {
        identity.setCreatedAt(LocalDateTime.now());
        identity.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

}
