package com.yanyan.service.console.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.yanyan.Configs;
import com.yanyan.core.BusinessException;
import com.yanyan.data.domain.console.Field;
import com.yanyan.data.domain.console.ModuleCodes;
import com.yanyan.data.domain.console.ModuleSetup;
import com.yanyan.service.BaseService;
import com.yanyan.service.console.GenerateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO:
 * 1、按照intelliJ项目分Module保存，下载
 * 2、关联其他表的名称
 * 3、子表
 * 4、树形结构
 * 5、String默认提供@Size检查
 * User: Saintcy
 * Date: 2016/10/25
 * Time: 11:18
 */
@Slf4j
@Service
public class GenerateServiceImpl extends BaseService implements GenerateService {
    @Autowired
    private Gson gson;

    public ModuleCodes generate(ModuleSetup setup) {
    	
    
    	
        try {
            String basePath = Configs.BASE_FILE_PATH + "/codegen/" + setup.getName();
            List<String> domains = generateDomain(basePath, setup);
            List<String> queries = generateQuery(basePath, setup);
            List<String> persists = generatePersist(basePath, domains, queries, setup);
            List<String> services = generateService(basePath, domains, queries, persists, setup);
            generateController(basePath, domains, queries, services, setup);

        } catch (Exception e) {
            throw new BusinessException("{generate.code.error}", e);
        }


        return null;
    }

    public List<String> generateDomain(String basePath, ModuleSetup setup) throws IOException {
        List<String> domains = Lists.newArrayList();

        String className = setup.getName();
        String packagePath = StringUtils.isEmpty(setup.getPackageName()) ? "" : (StringUtils.replace(setup.getPackageName(), ".", "/") + "/");
        String modulePath = StringUtils.isEmpty(setup.getModuleName()) ? "" : (StringUtils.replace(setup.getModuleName(), ".", "/") + "/");
        String classFileName = packagePath + "data/domain/" + modulePath + className + ".java";
        String classPackageName = (StringUtils.isEmpty(setup.getPackageName()) ? "" : (setup.getPackageName() + "."))
                + "data.domain." + (StringUtils.isEmpty(setup.getModuleName()) ? "" : setup.getModuleName());


        boolean extendsBaseDomain = false;
        StringBuffer sbCode = new StringBuffer();
        StringBuffer sbTemp = new StringBuffer();
        StringBuffer sbDefault = new StringBuffer();
        StringBuffer sbInputOnly = new StringBuffer();
        boolean hasInputOnly = false;
        StringBuffer sbOutputOnly = new StringBuffer();
        boolean hasOutputOnly = false;
        StringBuffer sbTransient = new StringBuffer();
        boolean hasTransient = false;
        Map<String, String> imports = new HashMap();

        String packageLine = "package " + classPackageName + ";";

        imports.put("lombok.Data", "lombok.Data");
        imports.put("com.yanyan.core.spring.validator.group.Create", "com.yanyan.core.spring.validator.group.Create");
        imports.put("com.yanyan.core.spring.validator.group.Update", "com.yanyan.core.spring.validator.group.Update");

        sbInputOnly.append("    /**Input Only Parameters*/\n");
        sbInputOnly.append("    /***/\n");

        sbOutputOnly.append("    /**Output Only Parameters*/\n");
        sbOutputOnly.append("    /***/\n");

        sbTransient.append("    /**Transient(Not Input and Output) Parameters*/\n");
        sbTransient.append("    /***/\n");

        for (Field field : setup.getFields()) {
            if (StringUtils.equalsIgnoreCase(field.getTransfer(), "None")) {
                continue;
            }
            /*field.setName(field.getName().toLowerCase());
            if(setup.getIsCamelCase()){
                field.setName(underlineToCamel(field.getName()));
            }*/

            if (StringUtils.equals("create_time", field.getName()) || StringUtils.equals("update_time", field.getName())) {
                imports.put("BaseDomain", "com.yanyan.core.lang.BaseDomain");
                extendsBaseDomain = true;
                continue;
            }
            sbTemp.append("    /**\n");
            sbTemp.append("      * \n");
            sbTemp.append("      * ").append(StringUtils.replace(field.getDesc(), "*/", "* /")).append("\n");
            sbTemp.append("      * \n");
            sbTemp.append("      */\n");
            String shortType = imports(field.getType(), imports);
            if (field.getConstraints() != null) {
                imports.put("hibernate.constraints", "org.hibernate.validator.constraints.*");
                imports.put("javax.constraints", "javax.validation.constraints.*");
                for (String constraint : field.getConstraints()) {
                    sbTemp.append("    ").append(getConstraintCode(constraint)).append("\n");
                }
            }

            if (StringUtils.equalsIgnoreCase(field.getTransfer(), "InputOnly")) {
                imports.put("InputOnly", "com.yanyan.core.serialize.exclusion.annotation.InputOnly");
                sbInputOnly.append("    @InputOnly\n");
                sbInputOnly.append(sbTemp);
                sbInputOnly.append("    private ").append(shortType).append(" ").append(field.getName()).append(";").append("\n");
                hasInputOnly = true;
            } else if (StringUtils.equalsIgnoreCase(field.getTransfer(), "OutputOnly")) {
                imports.put("OutputOnly", "com.yanyan.core.serialize.exclusion.annotation.OutputOnly");
                sbOutputOnly.append(sbTemp);
                sbOutputOnly.append("    @OutputOnly\n");
                sbOutputOnly.append("    private ").append(shortType).append(" ").append(field.getName()).append(";").append("\n");

                hasOutputOnly = true;
            } else if (StringUtils.equalsIgnoreCase(field.getTransfer(), "NoTransfer")) {
                imports.put("NoTransfer", "com.yanyan.core.serialize.exclusion.annotation.NoTransfer");
                sbTransient.append(sbTemp);
                sbTransient.append("    @NoTransfer\n");
                sbTransient.append("    private ").append(shortType).append(" ").append(field.getName()).append(";").append("\n");

                hasTransient = true;
            } else {
                sbDefault.append(sbTemp);
                sbDefault.append("    private ").append(shortType).append(" ").append(field.getName()).append(";").append("\n");
            }
            sbTemp.setLength(0);
        }

        sbCode.append(packageLine).append("\n\n");
        for (String imp : imports.values()) {
            sbCode.append("import ").append(imp).append(";\n");
        }

        sbCode.append("\n");
        sbCode.append("/**\n");
        sbCode.append(" * ").append(setup.getTitle()).append("\n");
        sbCode.append(" * User: ").append(setup.getAuthor()).append("\n");
        sbCode.append(" * Date: ").append(DateFormatUtils.format(new Date(), "yyyy/MM/dd")).append("\n");
        sbCode.append(" * Time: ").append(DateFormatUtils.format(new Date(), "HH:mm")).append("\n");
        sbCode.append(" */\n");
        sbCode.append("@Data\n");
        sbCode.append("public class ").append(className).append(extendsBaseDomain ? " extends BaseDomain" : "").append(" {\n");
        sbCode.append(sbDefault).append("\n");
        sbCode.append(sbInputOnly).append(hasInputOnly ? "" : "    //No fields\n").append("\n");
        sbCode.append(sbOutputOnly).append(hasOutputOnly ? "" : "    //No fields\n").append("\n");
        sbCode.append(sbTransient).append(hasTransient ? "" : "    //No fields\n").append("\n");
        sbCode.append("}");

        FileUtils.write(new File(basePath + "/" + classFileName), sbCode.toString(), "utf-8");
        domains.add(classPackageName + "." + className);

        if (setup.getSlaveTable() != null) {
            for (ModuleSetup setup1 : setup.getSlaveTable()) {
                domains.addAll(generateDomain(basePath, setup1));
            }
        }
        return domains;
    }

    public List<String> generateQuery(String basePath, ModuleSetup setup) throws IOException {
        List<String> queries = Lists.newArrayList();

        String className = setup.getName() + "Query";
        String packagePath = StringUtils.isEmpty(setup.getPackageName()) ? "" : (StringUtils.replace(setup.getPackageName(), ".", "/") + "/");
        String modulePath = StringUtils.isEmpty(setup.getModuleName()) ? "" : (StringUtils.replace(setup.getModuleName(), ".", "/") + "/");
        String classFileName = packagePath + "data/query/" + modulePath + className + ".java";
        String classPackageName = (StringUtils.isEmpty(setup.getPackageName()) ? "" : (setup.getPackageName() + "."))
                + "data.query." + (StringUtils.isEmpty(setup.getModuleName()) ? "" : setup.getModuleName());

        Map<String, String> imports = new HashMap();
        String keyColumns = "";
        StringBuffer sbCode = new StringBuffer();
        StringBuffer sbTemp = new StringBuffer();


        String packageLine = "package " + classPackageName + ";";
        imports.put("lombok.Data", "lombok.Data");
        if (setup.getIsPaginate()) {
            imports.put("com.yanyan.core.lang.PageQuery", "com.yanyan.core.lang.PageQuery");
        }

        for (Field field : setup.getFields()) {
            boolean containsUnderline = StringUtils.contains(field.getName(), "_");
            if (field.getQueryType() == 1 || field.getQueryType() == 3 || field.getQueryType() == 4) {
                String shortType = imports(field.getType(), imports);
                sbTemp.append("    /**\n");
                sbTemp.append("      * ").append(StringUtils.replaceEach(field.getDesc(), new String[]{"/*", "*/"}, new String[]{"/ *", "* /"})).append("").append("\n");
                if (field.getQueryType() == 3) {
                    sbTemp.append("      * 多个用\",\"英文逗号隔开");
                }
                sbTemp.append("      * \n");
                sbTemp.append("      */\n");
                sbTemp.append("    private ").append(field.getQueryType() == 3 ? "String" : shortType).append(" ").append(field.getName()).append(";").append("\n");
            } else if (field.getQueryType() == 2) {
                String shortType = imports(field.getType(), imports);
                sbTemp.append("    /**\n");
                sbTemp.append("      * ").append(StringUtils.replaceEach(field.getDesc(), new String[]{"/*", "*/"}, new String[]{"/ *", "* /"})).append("开始").append("\n");
                sbTemp.append("      * \n");
                sbTemp.append("      */\n");
                sbTemp.append("    private ").append(shortType).append(" ").append(field.getName()).append(containsUnderline ? "_min" : "Min").append(";\n");
                sbTemp.append("    /**\n");
                sbTemp.append("      * ").append(StringUtils.replaceEach(field.getDesc(), new String[]{"/*", "*/"}, new String[]{"/ *", "* /"})).append("截止").append("\n");
                sbTemp.append("      * \n");
                sbTemp.append("      */\n");
                sbTemp.append("    private ").append(shortType).append(" ").append(field.getName()).append(containsUnderline ? "_max" : "Max").append(";\n");
            }
            if (field.getIsKeyword()) {
                if (StringUtils.isNotEmpty(keyColumns)) {
                    keyColumns += "、 ";
                }
                keyColumns += field.getName();
            }
        }
        if (StringUtils.isNotEmpty(keyColumns)) {
            sbTemp.append("    /**\n");
            sbTemp.append("      * ").append("模糊查询关键字，可查询").append(keyColumns).append("等字段\n");
            sbTemp.append("      * \n");
            sbTemp.append("      */\n");
            sbTemp.append("    private ").append("String").append(" ").append("keyword").append(";").append("\n");
        }

        sbCode.append(packageLine).append("\n\n");
        for (String imp : imports.values()) {
            sbCode.append("import ").append(imp).append(";\n");
        }

        sbCode.append("\n");
        sbCode.append("/**\n");
        sbCode.append(" * ").append(StringUtils.replaceEach(setup.getTitle(), new String[]{"/*", "*/"}, new String[]{"/ *", "* /"})).append("\n");
        sbCode.append(" * User: ").append(setup.getAuthor()).append("\n");
        sbCode.append(" * Date: ").append(DateFormatUtils.format(new Date(), "yyyy/MM/dd")).append("\n");
        sbCode.append(" * Time: ").append(DateFormatUtils.format(new Date(), "HH:mm")).append("\n");
        sbCode.append(" */\n");
        sbCode.append("@Data\n");
        sbCode.append("public class ").append(className).append(setup.getIsPaginate() ? " extends PageQuery" : "").append(" {\n");
        sbCode.append(sbTemp);
        sbCode.append("}\n");

        FileUtils.write(new File(basePath + "/" + classFileName), sbCode.toString(), "utf-8");
        queries.add(classPackageName + "." + className);

        if (setup.getSlaveTable() != null) {
            for (ModuleSetup setup1 : setup.getSlaveTable()) {
                queries.addAll(generateQuery(basePath, setup1));
            }
        }

        return queries;
    }

    public List<String> generatePersist(String basePath, List<String> domains, List<String> queries, ModuleSetup setup) throws IOException {
        List<String> persists = Lists.newArrayList();

        String domainName = setup.getName();
        String className = domainName + "Dao";
        String classImplName = domainName + "DaoImpl";
        String packagePath = StringUtils.isEmpty(setup.getPackageName()) ? "" : (StringUtils.replace(setup.getPackageName(), ".", "/") + "/");
        String modulePath = StringUtils.isEmpty(setup.getModuleName()) ? "" : (StringUtils.replace(setup.getModuleName(), ".", "/") + "/");
        String classFileName = packagePath + "persist/" + modulePath + className + ".java";
        String classImplFileName = packagePath + "persist/" + modulePath + "impl/" + classImplName + ".java";
        String classPackageName = (StringUtils.isEmpty(setup.getPackageName()) ? "" : (setup.getPackageName() + "."))
                + "persist." + (StringUtils.isEmpty(setup.getModuleName()) ? "" : setup.getModuleName());
        String classImplPackageName = (StringUtils.isEmpty(setup.getPackageName()) ? "" : (setup.getPackageName() + "."))
                + "persist." + (StringUtils.isEmpty(setup.getModuleName()) ? "" : (setup.getModuleName() + ".")) + "impl";

        Map<String, String> imports = new HashMap();
        StringBuffer sbTemp = new StringBuffer();
        StringBuffer sbCode = new StringBuffer();
        StringBuffer sbSql = new StringBuffer();
        String domainClassName = domainName;
        String domainObjectName = StringUtils.uncapitalize(domainClassName);
        String domainNameUnderlineUpperCase = camelToUnderline(domainName).toUpperCase();

        String packageLine = "package " + classPackageName + ";";

        for (String domain : domains) {
            imports.put(domain, domain);
        }

        for (String query : queries) {
            imports.put(query, query);
        }

        if (setup.getIsPaginate()) {
            imports.put("com.yanyan.core.lang.Page", "com.yanyan.core.lang.Page");
        } else {
            imports.put("java.util.List", "java.util.List");
        }

        /**
         * 接口
         */
        sbCode.append(packageLine).append("\n\n");
        for (String imp : imports.values()) {
            sbCode.append("import ").append(imp).append(";\n");
        }

        sbCode.append("\n");
        sbCode.append("/**\n");
        sbCode.append(" * ").append(StringUtils.replaceEach(setup.getTitle(), new String[]{"/*", "*/"}, new String[]{"/ *", "* /"})).append("数据存储").append("\n");
        sbCode.append(" * User: ").append(setup.getAuthor()).append("\n");
        sbCode.append(" * Date: ").append(DateFormatUtils.format(new Date(), "yyyy/MM/dd")).append("\n");
        sbCode.append(" * Time: ").append(DateFormatUtils.format(new Date(), "HH:mm")).append("\n");
        sbCode.append(" */\n");
        sbCode.append("public interface ").append(className).append(" {\n");
        sbCode.append("    /**").append("\n");
        sbCode.append("     * 增加").append(setup.getTitle()).append("\n");
        sbCode.append("     *").append("\n");
        sbCode.append("     * @param ").append(domainObjectName).append("\n");
        sbCode.append("     * @return id").append("\n");
        sbCode.append("     */").append("\n");
        sbCode.append("    void insert").append(domainName).append("(").append(domainClassName).append(" ").append(domainObjectName).append(");\n");
        sbCode.append("    /**").append("\n");
        sbCode.append("     * 更新").append(setup.getTitle()).append("\n");
        sbCode.append("     *").append("\n");
        sbCode.append("     * @param ").append(WordUtils.uncapitalize(domainName)).append("\n");
        sbCode.append("     */").append("\n");
        sbCode.append("    void update").append(domainName).append("(").append(domainClassName).append(" ").append(domainObjectName).append(");\n");
        sbCode.append("    /**").append("\n");
        sbCode.append("     * 删除").append(setup.getTitle()).append("\n");
        sbCode.append("     *").append("\n");
        sbCode.append("     * @param ").append("id").append("\n");
        sbCode.append("     */").append("\n");
        sbCode.append("    void delete").append(domainName).append("(").append("long ").append("id").append(");\n");
        sbCode.append("    /**").append("\n");
        sbCode.append("     * 获取").append(setup.getTitle()).append("\n");
        sbCode.append("     *").append("\n");
        sbCode.append("     * @param ").append("id").append("\n");
        sbCode.append("     * @return").append("\n");
        sbCode.append("     */").append("\n");
        sbCode.append("    ").append(domainClassName).append(" get").append(domainName).append("(").append("long ").append("id").append(");\n");
        sbCode.append("    /**").append("\n");
        sbCode.append("     * 查找").append(setup.getTitle()).append("\n");
        sbCode.append("     *").append("\n");
        sbCode.append("     * @param ").append("query").append("\n");
        sbCode.append("     * @return").append("\n");
        sbCode.append("     */").append("\n");
        if (setup.getIsPaginate()) {
            sbCode.append("    Page<" + domainClassName + "> find").append(domainName).append("(").append(domainName).append("Query query").append(");\n");
        } else {
            sbCode.append("    List<" + domainClassName + "> find").append(domainName).append("(").append(domainName).append("Query query").append(");\n");
        }
        sbCode.append("}\n");

        FileUtils.write(new File(basePath + "/" + classFileName), sbCode.toString(), "utf-8");
        persists.add(classPackageName + "." + className);

        /**
         * 实现
         */
        sbTemp.setLength(0);
        sbCode.setLength(0);
        sbSql.setLength(0);
        StringBuffer sbKeyword = new StringBuffer();
        StringBuilder sbOrderBy = new StringBuilder();
        imports.put("lombok.extern.slf4j.Slf4j", "lombok.extern.slf4j.Slf4j");
        imports.put("org.springframework.stereotype.Repository", "org.springframework.stereotype.Repository");
        imports.put(classPackageName + "." + className, classPackageName + "." + className);
        imports.put("java.util.Map", "java.util.Map");
        imports.put("com.yanyan.core.db.NamedParameterJdbcDaoSupport", "com.yanyan.core.db.NamedParameterJdbcDaoSupport");
        imports.put("com.yanyan.core.db.NamedSqlBuilder", "com.yanyan.core.db.NamedSqlBuilder");
        imports.put("com.yanyan.core.db.ParameterBuilder", "com.yanyan.core.db.ParameterBuilder");
        imports.put("com.yanyan.core.db.ReflectiveRowMapperUtils", "com.yanyan.core.db.ReflectiveRowMapperUtils");

        packageLine = "package " + classImplPackageName + ";";
        //增加
        sbSql.append("    private static final String INSERT_").append(domainNameUnderlineUpperCase).append(" = \n");
        sbSql.append("            \"INSERT INTO ").append(setup.getTableName()).append("\"+\n");
        sbSql.append("            \"  (");
        for (Field field : setup.getFields()) {
            sbSql.append(field.getColumnName()).append(", ");
        }
        sbSql.setLength(sbSql.length() - 2);
        sbSql.append(")\\n\" +\n");
        sbSql.append("            \"VALUES\\n\" +\n");
        sbSql.append("            \"  (");
        for (Field field : setup.getFields()) {
            if (field.getIsInsert()) {
                sbSql.append(":").append(field.getName()).append(", ");
            }
        }
        sbSql.setLength(sbSql.length() - 2);
        sbSql.append(")\";\n");

        //修改
        sbSql.append("    private static final String UPDATE_").append(domainNameUnderlineUpperCase).append(" = \n");
        sbSql.append("            \"UPDATE ").append(setup.getTableName()).append("\\n\" +\n");
        sbSql.append("            \"   SET ");
        for (Field field : setup.getFields()) {
            if (field.getIsUpdate()) {
                sbSql.append(field.getColumnName()).append(" = :").append(field.getName()).append(", ");
            }
        }
        sbSql.setLength(sbSql.length() - 2);
        sbSql.append("\\n\" +\n");
        sbSql.append("            \" WHERE id = :id\";\n");

        //删除
        sbSql.append("    private static final String DELETE_").append(domainNameUnderlineUpperCase).append(" = \n");
        if (setup.getIsFlagDelete()) {
            sbSql.append("            \"UPDATE ").append(setup.getTableName()).append(" SET is_del = 1 WHERE id = :id\";\n");
        } else {
            sbSql.append("            \"DELETE FROM ").append(setup.getTableName()).append(" WHERE id = :id\"").append(";\n");
        }

        //查询
        sbSql.append("    private static final String GET_").append(domainNameUnderlineUpperCase).append(" = \n");
        sbSql.append("            \"SELECT ");
        for (Field field : setup.getFields()) {
            //if (field.getIsSelect()) {
            if (StringUtils.equalsIgnoreCase(setup.getTableName(), field.getTableName())) {
                sbSql.append("a.").append(field.getColumnName()).append(StringUtils.equalsIgnoreCase(field.getColumnName(), field.getName()) ? "" : " as " + field.getName()).append(", ");
            }
            //}
        }
        sbSql.setLength(sbSql.length() - 2);
        sbSql.append("\" +\n");
        sbSql.append("            \"  FROM ").append(setup.getTableName()).append(" a\\n\" +\n");
        sbSql.append("            \" WHERE 1 = 1\\n\";\n");

        sbTemp.append("    public void insert").append(domainName).append("(").append(domainClassName).append(" ").append(domainObjectName).append(") {\n");
        sbTemp.append("        Map<String, Object> parameters = new ParameterBuilder(").append(domainObjectName).append(").create();\n");
        sbTemp.append("        this.getNamedParameterJdbcTemplate().update(INSERT_").append(domainNameUnderlineUpperCase).append(", parameters);\n");
        sbTemp.append("        ").append(domainObjectName).append(".setId(this.getNamedParameterJdbcTemplate().queryForObject(\"select @@IDENTITY\", Long.class));\n");
        //sbTemp.append("        return ").append(domainObjectName).append(".getId();\n");
        sbTemp.append("    }\n\n");
        sbTemp.append("    public void update").append(domainName).append("(").append(domainClassName).append(" ").append(domainObjectName).append(") {\n");
        sbTemp.append("        Map<String, Object> parameters = new ParameterBuilder(").append(domainObjectName).append(").create();\n");
        sbTemp.append("        this.getNamedParameterJdbcTemplate().update(UPDATE_").append(domainNameUnderlineUpperCase).append(", parameters);\n");
        sbTemp.append("    }\n\n");
        sbTemp.append("    public void delete").append(domainName).append("(").append("long ").append("id").append("){\n");
        sbTemp.append("        Map<String, Object> parameters = new ParameterBuilder(\"id\", id).create();\n");
        sbTemp.append("        this.getNamedParameterJdbcTemplate().update(DELETE_").append(domainNameUnderlineUpperCase).append(", parameters);\n");
        sbTemp.append("    }\n\n");
        sbTemp.append("    public ").append(domainClassName).append(" get").append(domainName).append("(").append("long ").append("id").append(") {\n");
        sbTemp.append("        try {\n");
        sbTemp.append("            NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_").append(domainNameUnderlineUpperCase).append(");\n");
        sbTemp.append("            sqlBuilder.append(\" AND a.id = :id\");\n");
        sbTemp.append("            ").append(domainClassName).append(" ").append(domainObjectName).append(" = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), new ParameterBuilder(\"id\", id).create(), ReflectiveRowMapperUtils.getRowMapper(").append(domainClassName).append(".class));\n");
        sbTemp.append("            \n");
        sbTemp.append("            return ").append(domainObjectName).append(";\n");
        sbTemp.append("        } catch (EmptyResultDataAccessException e) {\n");
        sbTemp.append("            return null;\n");
        sbTemp.append("        }\n");
        sbTemp.append("    }\n\n");
        imports.put("org.springframework.dao.EmptyResultDataAccessException", "org.springframework.dao.EmptyResultDataAccessException");
        if (setup.getIsPaginate()) {
            sbTemp.append("    public Page<" + domainClassName + "> find").append(domainName).append("(").append(domainName).append("Query query").append("){\n");
        } else {
            sbTemp.append("    public List<" + domainClassName + "> find").append(domainName).append("(").append(domainName).append("Query query").append("){\n");
        }
        sbTemp.append("            NamedSqlBuilder builder = new NamedSqlBuilder(GET_").append(domainNameUnderlineUpperCase).append(");\n");
        sbTemp.append("            if (query != null) {\n");
        for (Field field : setup.getFields()) {
            boolean containsUnderline = StringUtils.contains(field.getName(), "_");
            if (field.getQueryType() == 1) {
                sbTemp.append("                builder.appendIfNotEmpty(\" and a.").append(field.getColumnName()).append(" = :").append(field.getName()).append("\", query.get").append(StringUtils.capitalize(field.getName())).append("());\n");
            } else if (field.getQueryType() == 2) {
                sbTemp.append("                builder.appendIfNotEmpty(\" and a.").append(field.getColumnName()).append(" >= :").append(field.getName()).append(containsUnderline ? "_min" : "Min").append("\", query.get").append(StringUtils.capitalize(field.getName())).append(containsUnderline ? "_min" : "Min").append("());\n");
                sbTemp.append("                builder.appendIfNotEmpty(\" and a.").append(field.getColumnName()).append(" <= :").append(field.getName()).append(containsUnderline ? "_max" : "Max").append("\", query.get").append(StringUtils.capitalize(field.getName())).append(containsUnderline ? "_max" : "Max").append("());\n");
            } else if (field.getQueryType() == 3) {
                sbTemp.append("                builder.appendIfNotEmpty(\" and a.").append(field.getColumnName()).append(" in (:").append(field.getName()).append(")\", \"").append(field.getName()).append("\", StringUtils.split(query.get").append(StringUtils.capitalize(field.getName())).append("(), \",\")").append(");\n");
                imports.put("org.apache.commons.lang3.StringUtils", "org.apache.commons.lang3.StringUtils");
            } else if (field.getQueryType() == 4) {
                sbTemp.append("                builder.appendIfNotEmpty(\" and a.").append(field.getColumnName()).append(" like concat('%', :").append(field.getName()).append(", '%')\", query.get").append(StringUtils.capitalize(field.getName())).append("());\n");
            }
            if (field.getIsKeyword()) {
                if (sbKeyword.length() > 0) {
                    sbKeyword.append(" or ");
                }
                sbKeyword.append("a.").append(field.getColumnName()).append(" like concat('%', :keyword, '%')");
            }
            if (StringUtils.isNotEmpty(field.getOrderBy())) {
                if (sbOrderBy.length() > 0) {
                    sbOrderBy.append(", ");
                }
                sbOrderBy.append("a.").append(field.getColumnName()).append(" ").append(field.getOrderBy());
            }
        }
        if (sbKeyword.length() > 0) {
            sbTemp.append("                builder.appendIfNotEmpty(\" and (").append(sbKeyword).append(")\", query.getKeyword());\n");
        }
        sbTemp.append("            }\n");
        if (sbOrderBy.length() > 0) {
            sbTemp.append("            builder.append(\" order by ").append(sbOrderBy).append("\");\n");
        } else {
            sbTemp.append("            //builder.append(\" order by a.create_time\");\n");
        }
        sbTemp.append("            \n");
        if (setup.getIsPaginate()) {
            sbTemp.append("            Page<").append(domainClassName).append("> page = this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(").append(domainClassName).append(".class));\n\n");
            sbTemp.append("            return page;\n");
        } else {
            sbTemp.append("            List<").append(domainClassName).append("> list = this.getNamedParameterJdbcTemplate().query(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(").append(domainClassName).append(".class));\n\n");
            sbTemp.append("            return list;\n");
        }
        sbTemp.append("    }\n");

        sbCode.append(packageLine).append("\n\n");
        for (String imp : imports.values()) {
            sbCode.append("import ").append(imp).append(";\n");
        }

        sbCode.append("\n");
        sbCode.append("/**").append("\n");
        sbCode.append(" * ").append(StringUtils.replaceEach(setup.getTitle(), new String[]{"/*", "*/"}, new String[]{"/ *", "* /"})).append("数据存储类").append("\n");
        sbCode.append(" * User: ").append(setup.getAuthor()).append("\n");
        sbCode.append(" * Date: ").append(DateFormatUtils.format(new Date(), "yyyy/MM/dd")).append("\n");
        sbCode.append(" * Time: ").append(DateFormatUtils.format(new Date(), "HH:mm")).append("\n");
        sbCode.append(" */\n");
        sbCode.append("@Slf4j\n");
        sbCode.append("@Repository\n");
        sbCode.append("public class ").append(classImplName).append(" extends NamedParameterJdbcDaoSupport").append(" implements ").append(className).append(" {\n");
        sbCode.append(sbSql);
        sbCode.append(sbTemp);
        sbCode.append("}").append("\n");

        FileUtils.write(new File(basePath + "/" + classImplFileName), sbCode.toString(), "utf-8");

        return persists;
    }

    public List<String> generateService(String basePath, List<String> domains, List<String> queries, List<String> persists, ModuleSetup setup) throws IOException {
        List<String> services = Lists.newArrayList();

        String domainName = setup.getName();
        String className = domainName + "Service";
        String classImplName = domainName + "ServiceImpl";
        String packagePath = StringUtils.isEmpty(setup.getPackageName()) ? "" : (StringUtils.replace(setup.getPackageName(), ".", "/") + "/");
        String modulePath = StringUtils.isEmpty(setup.getModuleName()) ? "" : (StringUtils.replace(setup.getModuleName(), ".", "/") + "/");
        String classFileName = packagePath + "service/" + modulePath + className + ".java";
        String classImplFileName = packagePath + "service/" + modulePath + "impl/" + classImplName + ".java";
        String classPackageName = (StringUtils.isEmpty(setup.getPackageName()) ? "" : (setup.getPackageName() + "."))
                + "service." + (StringUtils.isEmpty(setup.getModuleName()) ? "" : setup.getModuleName());
        String classImplPackageName = (StringUtils.isEmpty(setup.getPackageName()) ? "" : (setup.getPackageName() + "."))
                + "service." + (StringUtils.isEmpty(setup.getModuleName()) ? "" : (setup.getModuleName() + ".")) + "impl";

        Map<String, String> imports = new HashMap();
        StringBuffer sbTemp = new StringBuffer();
        StringBuffer sbCode = new StringBuffer();
        String domainClassName = domainName;
        String domainObjectName = StringUtils.uncapitalize(domainClassName);
        String daoClassName = domainClassName + "Dao";
        String daoObjectName = domainObjectName + "Dao";

        String packageLine = "package " + classPackageName + ";";

        for (String domain : domains) {
            imports.put(domain, domain);
        }

        for (String query : queries) {
            imports.put(query, query);
        }

        if (setup.getIsPaginate()) {
            imports.put("com.yanyan.core.lang.Page", "com.yanyan.core.lang.Page");
        } else {
            imports.put("java.util.List", "java.util.List");
        }

        /**
         * 接口
         */
        sbCode.append(packageLine).append("\n\n");
        for (String imp : imports.values()) {
            sbCode.append("import ").append(imp).append(";\n");
        }

        sbCode.append("\n");
        sbCode.append("/**").append("\n");
        sbCode.append(" * ").append(StringUtils.replaceEach(setup.getTitle(), new String[]{"/*", "*/"}, new String[]{"/ *", "* /"})).append("\n").append("业务服务").append("\n");
        sbCode.append(" * User: ").append(setup.getAuthor()).append("\n");
        sbCode.append(" * Date: ").append(DateFormatUtils.format(new Date(), "yyyy/MM/dd")).append("\n");
        sbCode.append(" * Time: ").append(DateFormatUtils.format(new Date(), "HH:mm")).append("\n");
        sbCode.append(" */\n");
        sbCode.append("public interface ").append(className).append(" {\n");
        sbCode.append("    /**").append("\n");
        sbCode.append("     * 创建").append(setup.getTitle()).append("\n");
        sbCode.append("     *").append("\n");
        sbCode.append("     * @param ").append(domainObjectName).append("\n");
        sbCode.append("     * @return id").append("\n");
        sbCode.append("     */").append("\n");
        sbCode.append("    long create").append(domainName).append("(").append(domainClassName).append(" ").append(domainObjectName).append(");\n");
        sbCode.append("    /**").append("\n");
        sbCode.append("     * 更新").append(setup.getTitle()).append("\n");
        sbCode.append("     *").append("\n");
        sbCode.append("     * @param ").append(WordUtils.uncapitalize(domainName)).append("\n");
        sbCode.append("     */").append("\n");
        sbCode.append("    void update").append(domainName).append("(").append(domainClassName).append(" ").append(domainObjectName).append(");\n");
        sbCode.append("    /**").append("\n");
        sbCode.append("     * 删除").append(setup.getTitle()).append("\n");
        sbCode.append("     *").append("\n");
        sbCode.append("     * @param ").append("id").append("\n");
        sbCode.append("     */").append("\n");
        sbCode.append("    void delete").append(domainName).append("(").append("long ").append("id").append(");\n");
        sbCode.append("    /**").append("\n");
        sbCode.append("     * 获取").append(setup.getTitle()).append("\n");
        sbCode.append("     *").append("\n");
        sbCode.append("     * @param ").append("id").append("\n");
        sbCode.append("     * @return").append("\n");
        sbCode.append("     */").append("\n");
        sbCode.append("    ").append(domainClassName).append(" get").append(domainName).append("(").append("long ").append("id").append(");\n");
        sbCode.append("    /**").append("\n");
        sbCode.append("     * 查找").append(setup.getTitle()).append("\n");
        sbCode.append("     *").append("\n");
        sbCode.append("     * @param ").append("query").append("\n");
        sbCode.append("     * @return").append("\n");
        sbCode.append("     */").append("\n");
        if (setup.getIsPaginate()) {
            sbCode.append("    Page<" + domainClassName + "> find").append(domainName).append("(").append(domainName).append("Query query").append(");\n");
        } else {
            sbCode.append("    List<" + domainClassName + "> find").append(domainName).append("(").append(domainName).append("Query query").append(");\n");
        }
        sbCode.append("}").append("\n");

        FileUtils.write(new File(basePath + "/" + classFileName), sbCode.toString(), "utf-8");
        services.add(classPackageName + "." + className);

        /**
         * 实现
         */
        sbTemp.setLength(0);
        sbCode.setLength(0);
        packageLine = "package " + classImplPackageName + ";";
        imports.put("lombok.extern.slf4j.Slf4j", "lombok.extern.slf4j.Slf4j");
        imports.put("org.springframework.stereotype.Service", "org.springframework.stereotype.Service");
        imports.put("org.springframework.beans.factory.annotation.Autowired", "org.springframework.beans.factory.annotation.Autowired");
        imports.put("com.yanyan.service.BaseService", "com.yanyan.service.BaseService");
        imports.put("com.yanyan.core.spring.validator.group.Create", "com.yanyan.core.spring.validator.group.Create");
        imports.put("com.yanyan.core.spring.validator.group.Update", "com.yanyan.core.spring.validator.group.Update");
        imports.put(classPackageName + "." + className, classPackageName + "." + className);
        for (String persist : persists) {
            imports.put(persist, persist);
        }

        sbTemp.append("    @Autowired\n");
        sbTemp.append("    private ").append(daoClassName).append(" ").append(daoObjectName).append(";\n\n");
        sbTemp.append("    public long create").append(domainName).append("(").append(domainClassName).append(" ").append(domainObjectName).append(") {\n");
        sbTemp.append("        validate(").append(domainObjectName).append(", Create.class);\n");
        sbTemp.append("        long id = ").append(daoObjectName).append(".insert").append(domainName).append("(").append(domainObjectName).append(");\n");
        sbTemp.append("        \n");
        sbTemp.append("        return id;\n");
        sbTemp.append("    }\n\n");
        sbTemp.append("    public void update").append(domainName).append("(").append(domainClassName).append(" ").append(domainObjectName).append(") {\n");
        sbTemp.append("        validate(").append(domainObjectName).append(", Update.class);\n");
        sbTemp.append("        ").append(daoObjectName).append(".update").append(domainName).append("(").append(domainObjectName).append(");\n");
        sbTemp.append("    }\n\n");
        sbTemp.append("    public void validate(").append(domainClassName).append(" ").append(domainObjectName).append(", Class<?>... groups) {\n");
        sbTemp.append("        super.validate(").append(domainObjectName).append(", groups").append(");\n\n");
        sbTemp.append("    }\n\n");
        sbTemp.append("    public void delete").append(domainName).append("(").append("long ").append("id").append("){\n");
        sbTemp.append("        ").append(daoObjectName).append(".delete").append(domainName).append("(id);\n");
        sbTemp.append("    }\n\n");
        sbTemp.append("    public ").append(domainClassName).append(" get").append(domainName).append("(").append("long ").append("id").append(") {\n");
        sbTemp.append("        ").append(domainClassName).append(" ").append(domainObjectName).append(" = ").append(daoObjectName).append(".get").append(domainName).append("(id);").append("\n\n");
        sbTemp.append("        return ").append(domainObjectName).append(";\n");
        sbTemp.append("    }\n\n");
        if (setup.getIsPaginate()) {
            sbTemp.append("    public Page<" + domainClassName + "> find").append(domainName).append("(").append(domainName).append("Query query").append("){\n");
        } else {
            sbTemp.append("    public List<" + domainClassName + "> find").append(domainName).append("(").append(domainName).append("Query query").append("){\n");
        }
        if (setup.getIsPaginate()) {
            sbTemp.append("        Page<").append(domainClassName).append("> page = ").append(daoObjectName).append(".find").append(domainName).append("(query);\n\n");
            sbTemp.append("        return page;\n");
        } else {
            sbTemp.append("        List<").append(domainClassName).append("> list = ").append(daoObjectName).append(".find").append(domainName).append("(query);\n\n");
            sbTemp.append("        return list;\n");
        }
        sbTemp.append("    }\n");

        sbCode.append(packageLine).append("\n\n");
        for (String imp : imports.values()) {
            sbCode.append("import ").append(imp).append(";\n");
        }

        sbCode.append("\n");
        sbCode.append("/**").append("\n");
        sbCode.append(" * ").append(StringUtils.replaceEach(setup.getTitle(), new String[]{"/*", "*/"}, new String[]{"/ *", "* /"})).append("业务服务").append("\n");
        sbCode.append(" * ").append(StringUtils.replaceEach(setup.getDesc(), new String[]{"/*", "*/"}, new String[]{"/ *", "* /"})).append("\n");
        sbCode.append(" * User: ").append(setup.getAuthor()).append("\n");
        sbCode.append(" * Date: ").append(DateFormatUtils.format(new Date(), "yyyy/MM/dd")).append("\n");
        sbCode.append(" * Time: ").append(DateFormatUtils.format(new Date(), "HH:mm")).append("\n");
        sbCode.append(" */\n");
        sbCode.append("@Slf4j\n");
        sbCode.append("@Service\n");
        sbCode.append("public class ").append(classImplName).append(" extends BaseService").append(" implements ").append(className).append(" {").append("\n");
        sbCode.append(sbTemp);
        sbCode.append("}").append("\n");

        FileUtils.write(new File(basePath + "/" + classImplFileName), sbCode.toString(), "utf-8");

        return services;
    }

    public List<String> generateController(String basePath, List<String> domains, List<String> queries, List<String> services, ModuleSetup setup) throws IOException {
        List<String> controllers = Lists.newArrayList();

        String domainName = setup.getName();
        String className = domainName + "Controller";
        String packagePath = StringUtils.isEmpty(setup.getPackageName()) ? "" : (StringUtils.replace(setup.getPackageName(), ".", "/") + "/");
        String modulePath = StringUtils.isEmpty(setup.getModuleName()) ? "" : (StringUtils.replace(setup.getModuleName(), ".", "/") + "/");
        String classFileName = packagePath + "web/controller/" + modulePath + className + ".java";
        String classPackageName = (StringUtils.isEmpty(setup.getPackageName()) ? "" : (setup.getPackageName() + "."))
                + "web.controller." + (StringUtils.isEmpty(setup.getModuleName()) ? "" : setup.getModuleName());
        String narrowMappingPath = modulePath + domainName.toLowerCase();

        Map<String, String> imports = new HashMap();
        StringBuffer sbTemp = new StringBuffer();
        StringBuffer sbCode = new StringBuffer();
        String domainClassName = domainName;
        String domainObjectName = StringUtils.uncapitalize(domainClassName);
        String serviceClassName = domainClassName + "Service";
        String serviceObjectName = domainObjectName + "Service";

        String packageLine = "package " + classPackageName + ";";

        for (String domain : domains) {
            imports.put(domain, domain);
        }

        for (String query : queries) {
            imports.put(query, query);
        }

        if (setup.getIsPaginate()) {
            imports.put("com.yanyan.core.lang.Page", "com.yanyan.core.lang.Page");
        } else {
            imports.put("java.util.List", "java.util.List");
        }

        imports.put("lombok.extern.slf4j.Slf4j", "lombok.extern.slf4j.Slf4j");
        imports.put("org.springframework.stereotype.Controller", "org.springframework.stereotype.Controller");
        imports.put("org.springframework.beans.factory.annotation.Autowired", "org.springframework.beans.factory.annotation.Autowired");
        imports.put("org.springframework.ui.Model", "org.springframework.ui.Model");
        imports.put("org.springframework.web.bind.annotation.*", "org.springframework.web.bind.annotation.*");
        imports.put("com.yanyan.core.web.DataResponse", "com.yanyan.core.web.DataResponse");
        imports.put("org.springframework.http.MediaType", "org.springframework.http.MediaType");
        for (String service : services) {
            imports.put(service, service);
        }

        sbTemp.append("    @Autowired\n");
        sbTemp.append("    private ").append(serviceClassName).append(" ").append(serviceObjectName).append(";\n");

        sbTemp.append("    @RequestMapping(\"/list\")\n");
        sbTemp.append("    public String list(@ModelAttribute(\"query\") ").append(domainName).append("Query query, Model model) {\n");
        if (setup.getIsPaginate()) {
            sbTemp.append("        query.defaultPageParam();\n");
            sbTemp.append("        Page<").append(domainClassName).append("> page = ").append(serviceObjectName).append(".find").append(domainName).append("(query);\n");
            sbTemp.append("        model.addAttribute(\"page\", page);\n");
        } else {
            sbTemp.append("        List<").append(domainClassName).append("> list = ").append(serviceObjectName).append(".find").append(domainName).append("(query);\n");
            sbTemp.append("        model.addAttribute(\"list\", list);\n");
        }

        sbTemp.append("        \n");
        sbTemp.append("        return \"/").append(narrowMappingPath).append("/list\";\n");
        sbTemp.append("    }\n\n");
        sbTemp.append("    @RequestMapping(\"/find\")\n");
        sbTemp.append("    public Model find(@ModelAttribute(\"query\") ").append(domainName).append("Query query) {\n");
        sbTemp.append("        try {\n");
        sbTemp.append("            Page<").append(domainClassName).append("> page = ").append(serviceObjectName).append(".find").append(domainName).append("(query);\n");
        sbTemp.append("            \n");
        sbTemp.append("            return DataResponse.success(\"page\", page);\n");
        sbTemp.append("        } catch (Exception e) {\n");
        sbTemp.append("            log.error(\"\", e);\n");
        sbTemp.append("            return DataResponse.failure(e);\n");
        sbTemp.append("        }\n");
        sbTemp.append("    }\n\n");

        sbTemp.append("    @RequestMapping(\"/create\")\n");
        sbTemp.append("    public String create(Model model) {\n");
        sbTemp.append("        \n");
        sbTemp.append("        return \"/").append(narrowMappingPath).append("/detail\";\n");
        sbTemp.append("    }\n\n");
        sbTemp.append("    @RequestMapping(value = \"/create\", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})\n");
        sbTemp.append("    @ResponseBody\n");
        sbTemp.append("    public Model create(@RequestBody ").append(domainClassName).append(" ").append(domainObjectName).append(") {\n");
        sbTemp.append("        try {\n");
        sbTemp.append("            long id = ").append(serviceObjectName).append(".create").append(domainName).append("(").append(domainObjectName).append(");\n");
        sbTemp.append("            \n");
        sbTemp.append("            return DataResponse.success(\"id\", id);\n");
        sbTemp.append("        } catch (Exception e) {\n");
        sbTemp.append("            log.error(\"\", e);\n");
        sbTemp.append("            return DataResponse.failure(e);\n");
        sbTemp.append("        }\n");
        sbTemp.append("    }\n\n");

        sbTemp.append("    @RequestMapping(\"/update\")\n");
        sbTemp.append("    public String update(@RequestParam Long id, Model model) {\n");
        sbTemp.append("        ").append(domainClassName).append(" ").append(domainObjectName).append(" = ").append(serviceObjectName).append(".get").append(domainName).append("(id);\n");
        sbTemp.append("        model.addAttribute(\"").append(domainObjectName).append("\", ").append(domainObjectName).append(");\n");
        sbTemp.append("        \n");
        sbTemp.append("        return \"/").append(narrowMappingPath).append("/detail\";\n");
        sbTemp.append("    }\n\n");
        sbTemp.append("    @RequestMapping(value = \"/update\", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})\n");
        sbTemp.append("    @ResponseBody\n");
        sbTemp.append("    public Model update(@RequestBody ").append(domainClassName).append(" ").append(domainObjectName).append(") {\n");
        sbTemp.append("        try {\n");
        sbTemp.append("            ").append(serviceObjectName).append(".update").append(domainName).append("(").append(domainObjectName).append(");\n");
        sbTemp.append("            \n");
        sbTemp.append("            return DataResponse.success();\n");
        sbTemp.append("        } catch (Exception e) {\n");
        sbTemp.append("            log.error(\"\", e);\n");
        sbTemp.append("            return DataResponse.failure(e);\n");
        sbTemp.append("        }\n");
        sbTemp.append("    }\n\n");

        sbTemp.append("    @RequestMapping(value = \"/delete/{id}\", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})\n");
        sbTemp.append("    @ResponseBody\n");
        sbTemp.append("    public Model delete").append(domainName).append("(@PathVariable ").append("Long ").append("id").append("){\n");
        sbTemp.append("        try {\n");
        sbTemp.append("            ").append(serviceObjectName).append(".delete").append(domainName).append("(id);\n");
        sbTemp.append("            \n");
        sbTemp.append("            return DataResponse.success();\n");
        sbTemp.append("        } catch (Exception e) {\n");
        sbTemp.append("            log.error(\"\", e);\n");
        sbTemp.append("            return DataResponse.failure(e);\n");
        sbTemp.append("        }\n");
        sbTemp.append("    }\n\n");
        //TODO: CHECK

        sbCode.append(packageLine).append("\n\n");
        for (String imp : imports.values()) {
            sbCode.append("import ").append(imp).append(";\n");
        }

        sbCode.append("\n");
        sbCode.append("/**").append("\n");
        sbCode.append(" * ").append(StringUtils.replaceEach(setup.getTitle(), new String[]{"/*", "*/"}, new String[]{"/ *", "* /"})).append("\n").append("业务服务").append("\n");
        sbCode.append(" * User: ").append(setup.getAuthor()).append("\n");
        sbCode.append(" * Date: ").append(DateFormatUtils.format(new Date(), "yyyy/MM/dd")).append("\n");
        sbCode.append(" * Time: ").append(DateFormatUtils.format(new Date(), "HH:mm")).append("\n");
        sbCode.append(" */\n");
        sbCode.append("@Slf4j\n");
        sbCode.append("@Controller\n");
        sbCode.append("@RequestMapping(\"").append(narrowMappingPath).append("\")").append("\n");
        sbCode.append("public class ").append(className).append(" {\n");
        sbCode.append(sbTemp);
        sbCode.append("}\n");

        FileUtils.write(new File(basePath + "/" + classFileName), sbCode.toString(), "utf-8");
        controllers.add(classPackageName + "." + className);

        return controllers;
    }

    public String getConstraintCode(String constraint) {
        Map<String, Object> params = gson.fromJson(constraint, Map.class);
        String name = (String) params.get("name");
        String message = (String) params.get("message");
        String groups = (String) params.get("groups");

        if (StringUtils.equalsIgnoreCase("NotNull", name)) {
            String param = "";
            if (StringUtils.isNotEmpty(message)) {
                param += separator(param) + "message=\"" + message + "\"";
            }
            if (StringUtils.isNotEmpty(groups)) {
                param += separator(param) + "groups=" + groups;
            }

            return "@NotNull" + parameter(param);
        } else if (StringUtils.equalsIgnoreCase("NotBlank", name)) {
            String param = "";
            if (StringUtils.isNotEmpty(message)) {
                param += separator(param) + "message=\"" + message + "\"";
            }
            if (StringUtils.isNotEmpty(groups)) {
                param += separator(param) + "groups=" + groups;
            }

            return "@NotBlank" + parameter(param);
        } else if (StringUtils.equalsIgnoreCase("Size", name)) {
            Integer max = params.get("max") == null ? null : NumberUtils.createInteger(String.valueOf(params.get("max")));
            Integer min = params.get("min") == null ? null : NumberUtils.createInteger(String.valueOf(params.get("min")));
            String param = "";
            if (min != null) {
                param += separator(param) + "min=" + min + "";
            }
            if (max != null) {
                param += separator(param) + "max=" + max + "";
            }
            if (StringUtils.isNotEmpty(message)) {
                param += separator(param) + "message=\"" + message + "\"";
            }
            if (StringUtils.isNotEmpty(groups)) {
                param += separator(param) + "groups=" + groups;
            }
            return "@Size" + parameter(param);
        } else if (StringUtils.equalsIgnoreCase("URL", name)) {
            String param = "";
            if (StringUtils.isNotEmpty(message)) {
                param += separator(param) + "message=\"" + message + "\"";
            }
            if (StringUtils.isNotEmpty(groups)) {
                param += separator(param) + "groups=" + groups;
            }
            return "@URL" + parameter(param);
        } else if (StringUtils.equalsIgnoreCase("Email", name)) {
            String param = "";
            if (StringUtils.isNotEmpty(message)) {
                param += separator(param) + "message=\"" + message + "\"";
            }
            if (StringUtils.isNotEmpty(groups)) {
                param += separator(param) + "groups=" + groups;
            }
            return "@Email" + parameter(param);
        } else if (StringUtils.equalsIgnoreCase("Mobile", name)) {
            String param = "";
            if (StringUtils.isNotEmpty(message)) {
                param += separator(param) + "message=\"" + message + "\"";
            }
            if (StringUtils.isNotEmpty(groups)) {
                param += separator(param) + "groups=" + groups;
            }
            return "@Mobile" + parameter(param);
        } else {
            log.warn("暂不支持的验证类型" + name);
            return "";
        }
    }

    private String imports(String type, Map<String, String> imports) {
        String shortType = StringUtils.substring(type, type.lastIndexOf(".") + 1);
        if (!isJavaLang(type)) {
            String actualType = StringUtils.replaceEach(type, new String[]{"[", "]", "@", " "}, new String[]{"", "", "", ""}).trim();
            //String actualShortType = StringUtils.replaceEach(shortType, new String[]{"[", "]", " "}, new String[]{"", "", ""});
            imports.put(actualType, actualType);
        }
        return shortType;
    }

    private String separator(String param) {
        if (StringUtils.isEmpty(param)) {
            return "";
        } else {
            return ", ";
        }
    }

    private String parameter(String param) {
        if (StringUtils.isEmpty(param)) {
            return "";
        } else {
            return "(" + param + ")";
        }
    }

    public boolean isJavaLang(String type) {
        return type.indexOf("java.lang") == 0 || type.indexOf(".") < 0;
    }

    public static final char UNDERLINE = '_';

    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i != 0) {
                    sb.append(UNDERLINE);
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
