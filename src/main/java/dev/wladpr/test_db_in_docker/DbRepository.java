package dev.wladpr.test_db_in_docker;

import java.util.List;

public interface DbRepository<T> {
    void save(T entity);
    List<T> fetchAll();
}
