package com.piaomiao.command.impl;

import com.piaomiao.command.PostCommand;
import com.piaomiao.dto.PostDTO;
import com.piaomiao.model.PostModel;
import com.piaomiao.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCommandImpl implements PostCommand {

    @Autowired
    private PostService postService;

    @Override
    public PostModel findById(Long id) {
        return postService.findById(id);
    }

    @Override
    public PostModel findByCode(String code) {
        return postService.findByCode(code);
    }

    @Override
    public List<PostModel> findAll() {
        return postService.findAll();
    }

    @Override
    public Long save(PostDTO postDTO) {
        PostModel postModel = new PostModel();
        postModel.setCode(postDTO.getCode());
        postModel.setName(postDTO.getName());
        postModel.setSort(postDTO.getSort());
        postModel.setStatus(postDTO.getStatus());
        postModel.setRemark(postDTO.getRemark());
        return postService.save(postModel);
    }

    @Override
    public void update(PostDTO postDTO) {
        PostModel postModel = new PostModel();
        postModel.setId(postDTO.getId());
        postModel.setCode(postDTO.getCode());
        postModel.setName(postDTO.getName());
        postModel.setSort(postDTO.getSort());
        postModel.setStatus(postDTO.getStatus());
        postModel.setRemark(postDTO.getRemark());
        postService.update(postModel);
    }

    @Override
    public void delete(Long id) {
        postService.delete(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            postService.delete(id);
        }
    }
}
