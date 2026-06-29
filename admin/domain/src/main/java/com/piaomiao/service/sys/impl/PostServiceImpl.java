package com.piaomiao.service.sys.impl;

import com.piaomiao.model.sys.PostModel;
import com.piaomiao.repository.sys.PostRepository;
import com.piaomiao.service.sys.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 岗位服务实现
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public PostModel findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public PostModel findByCode(String code) {
        return postRepository.findByCode(code);
    }

    @Override
    public List<PostModel> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Long save(PostModel postModel) {
        return postRepository.save(postModel);
    }

    @Override
    public void update(PostModel postModel) {
        postRepository.update(postModel);
    }

    @Override
    public void delete(Long id) {
        postRepository.delete(id);
    }
}
