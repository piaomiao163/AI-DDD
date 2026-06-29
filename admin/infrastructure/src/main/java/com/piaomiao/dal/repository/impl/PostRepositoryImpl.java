package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.piaomiao.dal.entity.PostDO;
import com.piaomiao.dal.mapper.PostMapper;
import com.piaomiao.model.sys.PostModel;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.repository.sys.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryImpl implements PostRepository {

    @Autowired
    private PostMapper postMapper;

    @Override
    public PostModel findById(Long id) {
        PostDO postDO = postMapper.selectById(id);
        return postDO != null ? postDO.toModel() : null;
    }

    @Override
    public PostModel findByCode(String code) {
        PostDO postDO = postMapper.selectByCode(code);
        return postDO != null ? postDO.toModel() : null;
    }

    @Override
    public List<PostModel> findAll() {
        List<PostDO> postDOList = postMapper.selectList(new QueryWrapper<>());
        return postDOList.stream()
                .map(PostDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Long save(PostModel postModel) {
        PostDO postDO = PostDO.fromModel(postModel);
        AuditFieldUtil.fillForInsert(postDO);
        postMapper.insert(postDO);
        return postDO.getId();
    }

    @Override
    public void update(PostModel postModel) {
        PostDO postDO = PostDO.fromModel(postModel);
        AuditFieldUtil.fillForUpdate(postDO);
        postMapper.updateById(postDO);
    }

    @Override
    public void delete(Long id) {
        postMapper.deleteById(id);
    }
}
