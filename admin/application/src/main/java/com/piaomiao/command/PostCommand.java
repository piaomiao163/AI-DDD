package com.piaomiao.command;

import com.piaomiao.dto.PostDTO;
import com.piaomiao.model.PostModel;

import java.util.List;

public interface PostCommand {
    PostModel findById(Long id);
    PostModel findByCode(String code);
    List<PostModel> findAll();
    Long save(PostDTO postDTO);
    void update(PostDTO postDTO);
    void delete(Long id);
    void deleteBatch(List<Long> ids);
}
