package com.web.blog.dao.packaging;

import com.web.blog.model.packaging.Pack;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PackDao extends JpaRepository<Pack, String> {
    
}