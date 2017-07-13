package com.yanyan.persist.impl;

import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.persist.TreeDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形DAO
 * 表结构中需至少含有id、parent_id、path字段
 * User: Saintcy
 * Date: 2016/5/21
 * Time: 23:50
 */
public abstract class TreeDaoImpl extends NamedParameterJdbcDaoSupport implements TreeDao {
    private static final String HAS_CHILDREN = "SELECT count(*) FROM %s WHERE parent_id = :parent_id";
    private static final String GENERATE_PATH = "SELECT CONCAT(IFNULL((SELECT path FROM %s WHERE id = :parent_id), ''), :parent_id, '/')";
    private static final String GET_PATH = "SELECT path FROM %s WHERE id = :id";
    private static final String UPDATE_PATH = "UPDATE %s SET path = :path, level = :level WHERE id = :id";
    private static final String UPDATE_DESCENDANTS_PATH = "UPDATE %s SET path = concat(:new_path, substring(path, length(:old_path)+1)), level = level + :n  WHERE path like concat(:old_path, :id, '/', '%%')";
    private static final String UPDATE_PRIORITY = "UPDATE %s SET priority = :priority WHERE id = :id";
    private static final String GET_DESCENDANTS = "SELECT id FROM %s WHERE path like concat((select path from %s where id = :id), :id, '/%%')";

    public String generatePath(Long parent_id) {
        return this.getNamedParameterJdbcTemplate().queryForObject(String.format(GENERATE_PATH, getTableName()), new ParameterBuilder("parent_id", parent_id).create(), String.class);
    }

    public String getPath(Long id) {
        return this.getNamedParameterJdbcTemplate().queryForObject(String.format(GET_PATH, getTableName()), new ParameterBuilder("id", id).create(), String.class);
    }

    public void updatePath(Long id, String path) {
        this.getNamedParameterJdbcTemplate().update(String.format(UPDATE_PATH, getTableName()), new ParameterBuilder().put("id", id).put("path", path).put("level", StringUtils.split(path, "/").length).create());
    }

    public void updateDescendantsPath(Long id, String newPath, String oldPath) {
        this.getNamedParameterJdbcTemplate().update(String.format(UPDATE_DESCENDANTS_PATH, getTableName()), new ParameterBuilder().put("new_path", newPath).put("old_path", oldPath).put("n", StringUtils.split(newPath, "/").length - StringUtils.split(oldPath, "/").length).put("id", id).create());
    }

    public void updatePriority(Long id, Integer priority) {
        this.getNamedParameterJdbcTemplate().update(String.format(UPDATE_PRIORITY, getTableName()), new ParameterBuilder().put("id", id).put("priority", priority).create());
    }

    public boolean hasChildren(Long id) {
        return this.getNamedParameterJdbcTemplate().queryForObject(String.format(HAS_CHILDREN, getTableName()), new ParameterBuilder("parent_id", id).create(), Long.class) != 0;
    }

    public List<Long> getAncestors(Long id) {
        String path = getPath(id);
        String[] aPath = StringUtils.split(path, "/");
        List<Long> ancestors = new ArrayList<Long>();

        if (aPath != null) {
            for (String ancestor_id : aPath) {
                if(StringUtils.isNotEmpty(ancestor_id)) {
                    ancestors.add(NumberUtils.createLong(ancestor_id));
                }
            }
        }

        return ancestors;
    }

    public List<Long> getDescendants(Long id) {
        return this.getNamedParameterJdbcTemplate().queryForList(String.format(GET_DESCENDANTS, getTableName()), new ParameterBuilder("id", id).create(), Long.class);
    }

    protected abstract String getTableName();
}
