package com.piaomiao.command.sys;

import com.piaomiao.dto.sys.PostDTO;
import com.piaomiao.model.sys.PostModel;

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
