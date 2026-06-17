package com.piaomiao.service;

import com.piaomiao.model.PostModel;

import java.util.List;

/**
 * 岗位服务接口
 */
public interface PostService {
    /**
     * 根据ID查询岗位
     * @param id 岗位ID
     * @return 岗位模型
     */
    PostModel findById(Long id);

    /**
     * 根据编码查询岗位
     * @param code 岗位编码
     * @return 岗位模型
     */
    PostModel findByCode(String code);

    /**
     * 查询所有岗位
     * @return 岗位列表
     */
    List<PostModel> findAll();

    /**
     * 保存岗位
     * @param postModel 岗位模型
     * @return 岗位ID
     */
    Long save(PostModel postModel);

    /**
     * 更新岗位
     * @param postModel 岗位模型
     */
    void update(PostModel postModel);

    /**
     * 删除岗位
     * @param id 岗位ID
     */
    void delete(Long id);
}
