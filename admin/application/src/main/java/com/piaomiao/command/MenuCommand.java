package com.piaomiao.command;

import com.piaomiao.dto.MenuDTO;
import com.piaomiao.model.MenuModel;

import java.util.List;

public interface MenuCommand {
    MenuModel findById(Long id);
    List<MenuModel> findAll();
    List<MenuModel> findTree();
    Long save(MenuDTO menuDTO);
    void update(MenuDTO menuDTO);
    void delete(Long id);
    void deleteBatch(List<Long> ids);
}
