package com.authLab.jsrAuth.repositories;

import com.authLab.jsrAuth.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
