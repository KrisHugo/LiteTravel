package interfaces;

import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MapService<T> {
    /*
     * 查询所有
     * */
    List<T> findAll();

    /*
     * 分页查询
     * */
    PageInfo<T> findPage(Integer page, Integer size);

    /*
     * 根据主键查询, 由于是多个表间的map, 所以需要多个主键进行筛选, 便干脆直接用条件查询来筛选
     * */
    @Deprecated
    T findById(Integer[] ids);

    /*
     * 增加
     * */
    void add(T t);

    /*
     * 修改
     * */
    void update(T t);

    /*
     * 删除, 由于是多个表间的map, 所以需要多个主键进行筛选, 便干脆直接用条件查询来筛选
     * */
    void delete(T t);
}
