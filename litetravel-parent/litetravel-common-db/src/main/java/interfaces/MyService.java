package interfaces;

import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MyService<T> {
    /*
     * 查询所有
     * */
    List<T> findAll();

    /*
     * 条件查询
     * */
    List<T> findList(T t);

    /*
     * 分页查询
     * */
    PageInfo<T> findPage(Integer page, Integer size);

    /*
     * 分页条件查询
     * */
    PageInfo<T> findPage(Integer page, Integer size, T t);

    /*
     * 根据主键查询
     * */
    T findById(Integer id);

    /*
     * 增加地址
     * */
    void add(T t);

    /*
     * 修改地址
     * */
    void update(T t);

    /*
     * 删除地址
     * */
    void delete(Integer id);

}
